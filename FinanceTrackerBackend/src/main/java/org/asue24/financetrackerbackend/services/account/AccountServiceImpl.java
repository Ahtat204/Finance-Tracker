package org.asue24.financetrackerbackend.services.account;

import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the {@link AccountService} interface.
 * <p>
 * Provides business logic for managing {@link Account} entities,
 * including creation, retrieval, update, and deletion operations.
 * This class interacts with the {@link AccountRepository} to persist
 * and fetch data from the database.
 * </p>
 */
@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new {@link Account} and saves it to the database.
     *
     * @param account the account entity to be created
     * @return the saved account with its generated ID
     */
    @Async
    @Override
    public CompletableFuture<Account> addAccount(Account account) {
        var Acc = accountRepository.save(account);
        return CompletableFuture.completedFuture(Acc);
    }

    /**
     * Deletes the given {@link Account} if it exists in the database.
     *
     * @param id the account entity to delete
     * @return {@code true} if the account was successfully deleted,
     * {@code false} if the account does not exist
     */
    @Async
    @Override
    public CompletableFuture<Boolean> deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return CompletableFuture.completedFuture(true);
        }
        return CompletableFuture.completedFuture(false);

    }

    /**
     * Updates an existing {@link Account} in the database.
     * <p>
     * If the account does not exist, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param id      the id of the account intended to be updated
     * @param account the account entity with updated values
     * @return the updated account entity
     * @throws IllegalArgumentException if the account does not exist
     */
    @Async
    @Override
    public CompletableFuture<Account> updateAccount(Long id, Account account) {

        if (accountRepository.existsById(id)) {
            var result = accountRepository.save(account);
            return CompletableFuture.completedFuture(result);
        } else {
            throw new IllegalArgumentException("Account not found for update.");
        }
    }

    /**
     * Retrieves an {@link Account} by its unique ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return the account entity if found, or {@code null} if not found
     */
    @Async
    @Override
    public CompletableFuture<Account> getAccountByAccountId(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException(accountId.toString()));
        return CompletableFuture.completedFuture(account);
    }


    /**
     * Retrieves all {@link Account} entities from the database.
     *
     * @return a list of all accounts
     */
    @Async
    @Override
    public CompletableFuture<List<Account>> getAccounts() {
        var accounts = accountRepository.findAll();
        return CompletableFuture.completedFuture(accounts);
    }
}
