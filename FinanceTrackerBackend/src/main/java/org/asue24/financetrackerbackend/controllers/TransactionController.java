package org.asue24.financetrackerbackend.controllers;

import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.services.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for managing financial transactions.
 * <p>
 * Provides endpoints to create, retrieve, update, and delete transactions.
 * All operations are handled asynchronously using {@link CompletableFuture}.
 * </p>
 */
@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Constructor for {@link TransactionController}.
     *
     * @param transactionService the service handling transaction-related operations
     */
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Retrieves a transaction by its ID asynchronously.
     *
     * @param id the ID of the transaction to retrieve
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with the requested transaction and HTTP status 200 (OK)
     */
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Transaction>> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransaction(id)
                .thenApply(transaction -> ResponseEntity.ok(transaction))
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new transaction asynchronously.
     *
     * @param transaction the transaction object to create
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with the created transaction and HTTP status 200 (OK)
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<Transaction>> createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction)
                .thenApply(createdTransaction -> ResponseEntity.ok(createdTransaction));
    }

    /**
     * Retrieves all transactions asynchronously.
     *
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with a list of all transactions and HTTP status 200 (OK)
     */
    @GetMapping
    public CompletableFuture<ResponseEntity<List<Transaction>>> getAllTransactions() {
        return transactionService.getAllTransactions()
                .thenApply(transactions -> ResponseEntity.ok(transactions));
    }

    /**
     * Updates an existing transaction by its ID asynchronously.
     *
     * @param id          the ID of the transaction to update
     * @param transaction the transaction object containing updated information
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with the updated transaction and HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Transaction>> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(id, transaction)
                .thenApply(updatedTransaction -> ResponseEntity.ok(updatedTransaction))
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a transaction by its ID asynchronously.
     *
     * @param id the ID of the transaction to delete
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with a boolean indicating success and HTTP status 200 (OK)
     */
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Boolean>> deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id)
                .thenApply(deleted -> ResponseEntity.ok(deleted))
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }
}
