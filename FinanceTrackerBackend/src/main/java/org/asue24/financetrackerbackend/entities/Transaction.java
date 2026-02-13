package org.asue24.financetrackerbackend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.asue24.enums.TransactionType;

import java.time.LocalDate;

/**
 * Represents a financial transaction linked to an account.
 * Each transaction has an amount, date, description, type,
 * and is associated with a specific account.
 *
 * <p>Transactions are typically created when a user performs
 * deposits, withdrawals, transfers, or other operations that
 * affect the account balance.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "transactions")
@Table(name = "transactions")
public class Transaction {
    /**
     * Unique identifier for the transaction (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long Id;

    /**
     * The amount of money involved in the transaction.
     */
    @Column(name = "amount", nullable = false)
    private Double amount;

    /**
     * The date on which the transaction occurred.
     */
    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    /**
     * An optional description providing context about the transaction.
     */
    @Column(name = "description", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String Description;

    /**
     * The type of transaction (e.g., INCOME, EXPENSE, TRANSFER).
     */
    @Enumerated(EnumType.STRING)
    @JsonFormat(pattern = "", shape = JsonFormat.Shape.NUMBER)
    @Column(name = "Transaction_type", nullable = true)
    private TransactionType Transactiontype;

    /**
     * The account associated with this transaction.
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Account.class)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
  //  @JsonIgnore
    private Account account;

    /**
     * Constructs a new Transaction with the given details.
     *
     * @param amount          the transaction amount
     * @param date            the transaction date
     * @param description     an optional description
     * @param transactiontype the type of transaction
     */
    public Transaction(Double amount, LocalDate date, String description, TransactionType transactiontype) {
       this.amount = amount;
        transactionDate = date;
        Description = description;
        Transactiontype = transactiontype;
    }

    public Transaction(long Id, double amount) {
     this.Id = Id;
     this.amount = amount;
    }
    public Transaction(Double amount, LocalDate date, String description, TransactionType transactiontype,Account account) {
        this.amount = amount;
        transactionDate = date;
        Description = description;
        Transactiontype = transactiontype;
        this.account = account;
    }

}

