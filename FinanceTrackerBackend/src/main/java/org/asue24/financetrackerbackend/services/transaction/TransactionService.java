package org.asue24.financetrackerbackend.services.transaction;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for managing {@link Transaction} entities.
 * <p>
 * Provides CRUD operations and business logic related to transactions.
 * The service layer acts as a bridge between the controller layer
 * and the persistence layer (repository).
 * </p>
 */
public interface TransactionService {

    /**
     * Creates a new {@link Transaction} and persists it in the database.
     *
     * @param transaction the transaction entity to create
     * @return the persisted {@link Transaction} with an assigned identifier
     */
    CompletableFuture<Transaction> createTransaction(Transaction transaction);

    /**
     * Deletes an existing {@link Transaction}.
     *
     * @param id the transaction id of the entity to delete
     * @return {@code true} if the transaction was successfully deleted,
     *         {@code false} otherwise
     */
    CompletableFuture<Boolean> deleteTransaction(Long id);

    /**
     * Updates the details of an existing {@link Transaction}.
     * @param id the id of the transaction for update
     * @param transaction the transaction entity containing updated information
     * @return the updated {@link Transaction} after persistence
     */
    CompletableFuture<Transaction> updateTransaction(Long id,Transaction transaction);

    /**
     * Retrieves a {@link Transaction}.
     * <p>
     * This method may retrieve the transaction based on its ID or
     * other identifying properties, depending on the implementation.
     * </p>
     *
     * @param transactionId the transaction entity to retrieve
     * @return the matching {@link Transaction}, or {@code null} if not found
     */
  CompletableFuture<  Transaction> getTransaction(Long transactionId) throws RuntimeException;

    /**
     * Retrieves all {@link Transaction} entities in the system.
     *
     * @return a list of all transactions; may be empty if no transactions exist
     */
    CompletableFuture<List<Transaction>> getAllTransactions();
}
