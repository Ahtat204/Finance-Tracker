package org.asue24.financetrackerbackend.repositories;

import org.asue24.financetrackerbackend.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository interface for managing {@link Transaction} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations, pagination,
 * and sorting. Spring Data JPA will automatically implement the methods
 * based on the method names.
 * </p>
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds a transaction by its transaction date.
     *
     * @param Date the date of the transaction to find
     * @return an {@link Optional} containing the transaction if found,
     *         or empty if no transaction exists on the given date
     */
    Optional<Transaction> findByTransactionDate(LocalDate Date);

    /**
     * Finds a transaction by its amount.
     *
     * @param Amount the amount of the transaction to find
     * @return an {@link Optional} containing the transaction if found,
     *         or empty if no transaction exists with the given amount
     */
    Optional<Transaction> findTransactionByAmount(Double Amount);
}
