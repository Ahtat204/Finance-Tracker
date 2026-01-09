package org.asue24.financetrackerbackend.services.transaction;

import jakarta.transaction.Transactional;
import org.asue24.enums.TransactionType;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.repositories.TransactionRepository;
import org.asue24.financetrackerbackend.services.account.AccountService;
import org.asue24.financetrackerbackend.services.caching.CachingService;
import org.asue24.financetrackerbackend.services.caching.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    /**
     *
     */
    private final CachingService<Transaction> redisService;

    private final AccountService accountService;
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
     * Creates and persists a new {@link Transaction}.
     *
     * @param transaction the transaction entity to create
     * @return the persisted transaction with an assigned identifier
     */
   // @CachePut(value = "transactions", key = "#result.id")
    @Transactional
    @Override
    public Transaction createTransaction(Transaction transaction) {
        var Trans = transactionRepository.save(transaction);
        var AccountId=Trans.getAccount().getId();
        accountService.UpdateAccount(AccountId,transaction.getAmount());
        redisService.put(Trans.getId().toString(), Trans);
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
        redisService.put(id.toString(), result);
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
        if (cached != null) return  cached;
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
