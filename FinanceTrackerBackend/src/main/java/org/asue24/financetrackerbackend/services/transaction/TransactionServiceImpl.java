package org.asue24.financetrackerbackend.services.transaction;

import jakarta.transaction.Transactional;
import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.repositories.TransactionRepository;
import org.asue24.financetrackerbackend.services.account.AccountService;
import org.asue24.financetrackerbackend.services.caching.CachingService;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link TransactionService} interface.
 * <p>
 * Provides business logic for managing {@link Transaction} entities,
 * including creating, updating, retrieving, and deleting transactions.
 * Interacts with {@link TransactionRepository} to persist and fetch data from the database.
 * </p>
 */

@Service
public class TransactionServiceImpl implements TransactionService {

    /**
     * Repository for performing CRUD operations on {@link Transaction} entities.
     */

    private final TransactionRepository transactionRepository;
    private final CachingService<Transaction> redisService;
    private final AccountService accountService;
    Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    /**
     * Constructs a new {@code TransactionServiceImpl} with the specified repository.
     *
     * @param transactionRepository the repository used to manage transactions
     */
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CachingService redisService, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.redisService = redisService;
        this.accountService = accountService;
    }

    /**
     * @param transaction the transaction entity to create
     * @param senderId
     * @param receiverId  it's optional because not always we transfer ;
     * @return
     */

    @Override
    public Transaction createTransaction(Transaction transaction, Integer senderId, Optional<Integer> receiverId) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        var type = transaction.getTransactiontype();
        transaction.setAccount(new Account(Long.valueOf(senderId)));
        switch (type) {
            case EXPENSE -> {
                var account = accountService.getAccountByAccountId(Long.valueOf(senderId));
                if (account.getbalance() < transaction.getAmount()) {
                    throw new IllegalArgumentException("not enough Balance");
                }
                account.setBalance(account.getbalance() - transaction.getAmount());
                accountService.updateAccount(Long.valueOf(senderId), account);
            }
            case INCOME -> {
                var account = accountService.getAccountByAccountId(Long.valueOf(senderId));
                account.setBalance(account.getbalance() + transaction.getAmount());
                accountService.updateAccount(Long.valueOf(senderId), account);
            }
            case TRANSFER -> {
                var SenderAccount = accountService.getAccountByAccountId(Long.valueOf(senderId));
                var ReceiverAccount = accountService.getAccountByAccountId(Long.valueOf(receiverId.get()));
                if (SenderAccount.getbalance() < transaction.getAmount()) {
                    throw new IllegalArgumentException("not enough Balance");
                }
                SenderAccount.setBalance(SenderAccount.getbalance() - transaction.getAmount());
                ReceiverAccount.setBalance(SenderAccount.getbalance() + transaction.getAmount());
                accountService.updateAccount(Long.valueOf(senderId), SenderAccount);
               var account= accountService.updateAccount(Long.valueOf(receiverId.get()), ReceiverAccount);
                logger.info("{account balance is:}"+account.getbalance());
            }
        }
        var Trans = transactionRepository.save(transaction);
        var AccountId = Trans.getAccount().getId();
        accountService.UpdateAccount(AccountId, transaction.getAmount());
        // redisService.put(Trans.getId().toString(), Trans);
        return Trans;
    }

    /**
     * Deletes an existing {@link Transaction} by its ID.
     *
     * @param id the transaction id of the entity to delete
     * @return {@code true} if the transaction was successfully deleted,
     * {@code false} if it does not exist
     */
    // @CacheEvict(value = "transactions", key = "#id")
    @Override
    public Boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            redisService.Evict(id.toString());
            return true;
        }
        return false;
    }

    /**
     * Updates an existing {@link Transaction}.
     * <p>
     * If the transaction does not exist, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param id          the id of the transaction to update
     * @param transaction the transaction entity with updated information
     * @return the updated transaction after persistence
     * @throws IllegalArgumentException if the transaction does not exist
     */
    // @CachePut(value = "transactions", key = "#id")
    @Override
    public Transaction updateTransaction(Long id, Transaction transaction) {
        var result = transactionRepository.findById(id)
                .map(existing -> {
                    transaction.setId(id); // ensure consistency
                    return transactionRepository.save(transaction);
                })
                .orElseThrow(() -> new IllegalArgumentException("Account not found for update."));
        return result;
    }

    /**
     * Retrieves a {@link Transaction} by its unique ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return the transaction if found, or {@code null} if not found
     */
    // @Cacheable(value = "transactions", key = "#transactionId")
    @Override
    public Transaction getTransaction(Long transactionId) throws RuntimeException {
        var cached = redisService.get(transactionId.toString());
        if (cached != null) return cached;
        var transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        redisService.put(transactionId.toString(), transaction);
        return transaction;
    }

    /**
     * Retrieves all {@link Transaction} entities in the system.
     *
     * @return a list of all transactions; may be empty if no transactions exist
     */

    @Override
    public List<Transaction> getAllTransactions() {
        var trans = transactionRepository.findAll();
        return trans;
    }
}
