package org.asue24.financetrackerbackend.controllers;

import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.services.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing financial accounts.
 * <p>
 * Provides endpoints to create, retrieve, update, and delete accounts.
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
     * Retrieves an account by its ID.
     *
     * @param id the ID of the account to retrieve
     * @return a {@link ResponseEntity}
     * with the requested account and HTTP status 200 (OK), or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
      return ResponseEntity.of(Optional.ofNullable(accountService.getAccountByAccountId(id)));
    }

    /**
     * Retrieves all accounts.
     *
     * @return a {@link ResponseEntity}
     * with a list of all accounts and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        var result = accountService.getAccounts();
        return ResponseEntity.ok(result);
    }

    /**
     * Updates an existing account by its ID.
     *
     * @param id      the ID of the account to update
     * @param account the account object containing updated information
     * @return a {@link ResponseEntity}
     * with the updated account and HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
       var result = accountService.updateAccount(id, account);
       if(result == null) {
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(result);
    }

    /**
     * Creates a new account.
     *
     * @param account the account object to create
     * @return a  {@link ResponseEntity}
     * with the created account and HTTP status 200 (OK)
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {

        var result = accountService.addAccount(account);
        if(result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    /**
     * Deletes an account by its ID.
     *
     * @param id the ID of the account to delete
     * @return a {@link ResponseEntity}
     * with a boolean indicating success and HTTP status 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
       return accountService.deleteAccount(id)?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }
}
