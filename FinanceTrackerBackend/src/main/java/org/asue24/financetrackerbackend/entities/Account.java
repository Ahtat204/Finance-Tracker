package org.asue24.financetrackerbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

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

    /**
     * Unique identifier for the account (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", unique = true, nullable = false, updatable = false)
    private Long id;

    /**
     * The display name of the account (e.g., "Savings Account").
     */
    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    /**
     * The current balance of the account.
     */
    @Column(name = "balance", nullable = false)
    private Double balance;


    /**
     * The user who owns this account.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = true, updatable = false)
    @JsonIgnore()
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "account")
    private Set<Transaction> transactions;
    /**

    /**
     * Constructs a new Account with the given name, balance, and user.
     *
     * @param accountName the account's name
     * @param balance     the initial balance
     * @param user        the owner of the account
     */
    public Account(Long id, String accountName, Double balance, User user) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
        this.user = user;
    }
    public Account( String accountName, Double balance, User user) {
        this.accountName = accountName;
        this.balance = balance;
        this.user = user;
    }

    public Account(Long id) {
        this.id = id;
    }
    public Double getbalance() {
        return balance;
    }
}
