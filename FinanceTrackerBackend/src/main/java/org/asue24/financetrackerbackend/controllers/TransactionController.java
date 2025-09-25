package org.asue24.financetrackerbackend.controllers;

import org.antlr.v4.runtime.misc.NotNull;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.services.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing financial transactions.
 * <p>
 * Provides endpoints to create, retrieve, update, and delete transactions.
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
     * Retrieves a transaction by its ID .
     *
     * @param id the ID of the transaction to retrieve
     * @return a  {@link ResponseEntity}
     * with the requested transaction and HTTP status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.of(Optional.ofNullable(transactionService.getTransaction(id)));
    }

    /**
     * Retrieves all transactions asynchronously.
     *
     * @return a {@link ResponseEntity}
     * with a list of all transactions and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        var result = transactionService.getAllTransactions();
        return ResponseEntity.ok(result);
    }

    /**
     * Creates a new transaction
     *
     * @param transaction the transaction object to create
     * @return a {@link ResponseEntity}
     * with the created transaction and HTTP status 200 (OK)
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@NotNull @RequestBody Transaction transaction) {
        var result = transactionService.createTransaction(transaction);
        if (result != null) return ResponseEntity.badRequest().body(result);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates an existing transaction by its ID
     *
     * @param id          the ID of the transaction to update
     * @param transaction the transaction object containing updated information
     * @return a {@link ResponseEntity}
     * with the updated transaction and HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        var updated = transactionService.updateTransaction(id, transaction);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a transaction by its ID asynchronously.
     *
     * @param id the ID of the transaction to delete
     * @return a {@link ResponseEntity}
     * with a boolean indicating success and HTTP status 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();

    }
}
