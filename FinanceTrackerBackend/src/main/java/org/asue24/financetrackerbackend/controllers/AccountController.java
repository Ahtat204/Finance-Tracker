package org.asue24.financetrackerbackend.controllers;

import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.services.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for managing financial accounts.
 * <p>
 * Provides endpoints to create, retrieve, update, and delete accounts.
 * All operations are handled asynchronously using {@link CompletableFuture}.
 * </p>
 */
@RestController
@RequestMapping("api/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * Constructor for {@link AccountController}.
     *
     * @param accountService the service handling account-related operations
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Retrieves an account by its ID asynchronously.
     *
     * @param id the ID of the account to retrieve
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with the requested account and HTTP status 200 (OK), or 404 if not found
     */
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Account>> getAccountById(@PathVariable Long id) {
        return accountService.getAccountByAccountId(id)
                .thenApply(account -> ResponseEntity.ok(account))
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all accounts asynchronously.
     *
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with a list of all accounts and HTTP status 200 (OK)
     */
    @GetMapping
    public CompletableFuture<ResponseEntity<List<Account>>> getAllAccounts() {
        return accountService.getAccounts()
                .thenApply(accounts -> ResponseEntity.ok(accounts));
    }

    /**
     * Updates an existing account by its ID asynchronously.
     *
     * @param id      the ID of the account to update
     * @param account the account object containing updated information
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with the updated account and HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Account>> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return accountService.updateAccount(id, account)
                .thenApply(updatedAccount -> ResponseEntity.ok(updatedAccount))
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new account asynchronously.
     *
     * @param account the account object to create
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with the created account and HTTP status 200 (OK)
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<Account>> createAccount(@RequestBody Account account) {
        return accountService.addAccount(account)
                .thenApply(createdAccount -> ResponseEntity.ok(createdAccount));
    }

    /**
     * Deletes an account by its ID asynchronously.
     *
     * @param id the ID of the account to delete
     * @return a {@link CompletableFuture} containing {@link ResponseEntity}
     *         with a boolean indicating success and HTTP status 200 (OK)
     */
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Boolean>> deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccount(id)
                .thenApply(deleted -> ResponseEntity.ok(deleted))
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }
}
