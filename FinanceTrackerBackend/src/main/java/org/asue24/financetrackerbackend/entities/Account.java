package org.asue24.financetrackerbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a financial account owned by a user.
 * Each account has a name, balance, and is linked to a specific user.
 *
 * <p>Accounts are containers for transactions. A user can
 * have multiple accounts, such as "Checking", "Savings",
 * or "Credit Card".</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "accounts")
@Table(name = "accounts")
public class Account {

    /** Unique identifier for the account (primary key). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", unique = true, nullable = false, updatable = false)
    private Long id;

    /** The display name of the account (e.g., "Savings Account"). */
    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    /** The current balance of the account. */
    @Column(name = "balance", nullable = false)
    private Double balance;

    /** The user who owns this account. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Constructs a new Account with the given name and balance.
     *
     * @param accountName the account's name
     * @param balance the initial balance
     */
    public Account(String accountName, Double balance) {
        this.accountName = accountName;
        this.balance = balance;
    }

    /**
     * Constructs a new Account with the given name, balance, and user.
     *
     * @param accountName the account's name
     * @param balance the initial balance
     * @param user the owner of the account
     */
    public Account(String accountName, Double balance, User user) {
        this.accountName = accountName;
        this.balance = balance;
        this.user = user;
    }
}
