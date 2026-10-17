package org.asue24.financetrackerbackend.services.account;
import org.asue24.financetrackerbackend.entities.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for managing {@link Account} entities.
 * <p>
 * Provides CRUD operations and business logic related to accounts.
 * The service layer acts as a bridge between the controller layer
 * and the persistence layer (repository).
 */
public interface AccountService {

    /**
     * Creates and persists a new {@link Account}.
     * <p>
     * The returned {@link Account} will contain any generated identifiers
     * (such as the primary key).
     *
     * @param account the account entity to be created
     * @return the persisted {@link Account} with an assigned identifier
     */
    Account addAccount(Account account);

    /**
     * Deletes an existing {@link Account}.
     *
     * @param id the account id to be deleted
     * @return {@code true} if the account was successfully deleted,
     *         {@code false} otherwise
     */
    Boolean deleteAccount(Long id);

    /**
     * Updates the details of an existing {@link Account}.
     * @param id the id of the entity intended to update
     * @param account the account entity containing updated information
     * @return the updated {@link Account} after persistence
     */
    Account updateAccount(Long id,Account account);

    double UpdateAccount(Long id,double amount);

    /**
     * Retrieves an {@link Account} by its unique identifier.
     *
     * @param accountId the unique identifier of the account
     * @return the {@link Account} if found, or {@code RunTimeException} if no account exists with the given ID
     */
    Account getAccountByAccountId(Long  accountId) throws RuntimeException;
    /**
     * Retrieves all {@link Account} entities in the system.
     *
     * @return a list of all accounts; may be empty if no accounts exist
     */
    List<Account> getAccounts();
}

