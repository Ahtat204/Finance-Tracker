package org.asue24.financetrackerbackend.services.account;

import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new {@link Account} and saves it to the database.
     *
     * @param account the account entity to be created
     * @return the saved account with its generated ID
     */
    @Override
    public Account addAccount(Account account) {
        var Acc = accountRepository.save(account);
        return Acc;
    }

    /**
     * Deletes the given {@link Account} if it exists in the database.
     *
     * @param account the account entity to delete
     * @return {@code true} if the account was successfully deleted,
     * {@code false} if the account does not exist
     */
    @Override
    public Boolean deleteAccount(Account account) {
        if (accountRepository.existsById(account.getId())) {
            accountRepository.delete(account);
            return true;
        }
        return false;

    }

    /**
     * Updates an existing {@link Account} in the database.
     * <p>
     * If the account does not exist, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param account the account entity with updated values
     * @return the updated account entity
     * @throws IllegalArgumentException if the account does not exist
     */
    @Override
    public Account updateAccount(Account account) {
        if (accountRepository.existsById(account.getId())) {
            return accountRepository.save(account);
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
    @Override
    public Account getAccountByAccountId(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    /**
     * Retrieves all {@link Account} entities from the database.
     *
     * @return a list of all accounts
     */
    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}
