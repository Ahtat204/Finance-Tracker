package org.asue24.financetrackerbackend.services.transaction;

import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
     * Constructs a new {@code TransactionServiceImpl} with the specified repository.
     *
     * @param transactionRepository the repository used to manage transactions
     */
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates and persists a new {@link Transaction}.
     *
     * @param transaction the transaction entity to create
     * @return the persisted transaction with an assigned identifier
     */
    @Async
    @Override
    public CompletableFuture<Transaction> createTransaction(Transaction transaction) {
        var Trans = transactionRepository.save(transaction);
        return CompletableFuture.completedFuture(transaction);
    }

    /**
     * Deletes an existing {@link Transaction} by its ID.
     *
     * @param id the transaction id of the entity to delete
     * @return {@code true} if the transaction was successfully deleted,
     * {@code false} if it does not exist
     */
    @Async
    @Override
    public CompletableFuture<Boolean> deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return CompletableFuture.completedFuture(true);
        }
        return CompletableFuture.completedFuture(false);
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
    @Async
    @Override
    public CompletableFuture<Transaction> updateTransaction(Long id, Transaction transaction) {
        var result = transactionRepository.findById(id)
                .map(existing -> {
                    transaction.setId(id); // ensure consistency
                    return transactionRepository.save(transaction);
                })
                .orElseThrow(() -> new IllegalArgumentException("Account not found for update."));
        return CompletableFuture.completedFuture(result);
    }

    /**
     * Retrieves a {@link Transaction} by its unique ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return the transaction if found, or {@code null} if not found
     */
    @Async
    @Override
    public CompletableFuture<Transaction> getTransaction(Long transactionId) throws RuntimeException {
        var transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        return CompletableFuture.completedFuture(transaction);
    }

    /**
     * Retrieves all {@link Transaction} entities in the system.
     *
     * @return a list of all transactions; may be empty if no transactions exist
     */
    @Async
    @Override
    public CompletableFuture<List<Transaction>> getAllTransactions() {
        var trans= transactionRepository.findAll();
        return CompletableFuture.completedFuture(trans);
    }
}
