package org.asue24.financetrackerbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.asue24.enums.TransactionType;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "transaction")
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    Long Id;
    @Column(name = "amount", nullable = false)
    Double Amount;
    @Column(name = "date", nullable = false)
    LocalDate Date;
    @Column(name = "description", nullable = true)
    String Description;
    @Enumerated(EnumType.STRING)
    TransactionType Transactiontype;
    Long AccountId;

    public Transaction(Double amount, LocalDate date, String description, TransactionType transactiontype, Long accountId) {
        Amount = amount;
        Date = date;
        Description = description;
        Transactiontype = transactiontype;

        AccountId = accountId;
    }
}
