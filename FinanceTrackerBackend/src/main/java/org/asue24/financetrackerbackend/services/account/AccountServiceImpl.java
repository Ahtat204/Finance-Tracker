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

    @Override
    public Account addAccount(Account account) {
        var Acc = accountRepository.save(account);
        if(Acc == null) throw new RuntimeException("Account not added");
        return account;
    }

    /**
     * Deletes the given {@link Account} if it exists in the database.
     *
     * @param id the account entity to delete
     * @return {@code true} if the account was successfully deleted,
     * {@code false} if the account does not exist
     */

    @Override
    public Boolean deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
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
     * @param id      the id of the account intended to be updated
     * @param account the account entity with updated values
     * @return the updated account entity
     * @throws IllegalArgumentException if the account does not exist
     */

    @Override
    public Account updateAccount(Long id, Account account) {

        var result = accountRepository.findById(id).map(existing->{
            account.setId(existing.getId());
            return accountRepository.save(account);
        }).orElseThrow(()->new RuntimeException("Account not found"));
        return result;

    }

    @Override
    public double UpdateAccount(Long id, double amount) {
        accountRepository.findById(id).ifPresent(existing->{
            existing.setBalance(existing.getbalance() + amount);
        });
        return amount;
    }

    /**
     * Retrieves an {@link Account} by its unique ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return the account entity if found, or {@code null} if not found
     */

    @Override
    public Account getAccountByAccountId(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException(accountId.toString()));
        return account;
    }


    /**
     * Retrieves all {@link Account} entities from the database.
     *
     * @return a list of all accounts
     */
    @Async
    @Override
    public List<Account> getAccounts() {
        var accounts = accountRepository.findAll();
        return accounts;
    }
}
