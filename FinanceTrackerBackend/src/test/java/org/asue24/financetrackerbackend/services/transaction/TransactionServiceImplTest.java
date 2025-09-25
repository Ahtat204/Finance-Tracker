package org.asue24.financetrackerbackend.services.transaction;

import org.asue24.enums.TransactionType;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;

    /// ///////////////////////////// happy path
    @Test
    void createTransaction() {
    }

    @Test
    void deleteTransaction() {
    }

    @Test
    void updateTransaction() {
    }

    @Test
    void getTransaction() {
    }

    @Test
    void getAllTransactions() {
    }
    /////////////
}