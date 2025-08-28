package org.asue24.financetrackerbackend.repositories;
import org.asue24.financetrackerbackend.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
