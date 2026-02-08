package org.asue24.financetrackerbackend.services.transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.asue24.enums.TransactionType;
import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.repositories.TransactionRepository;
import org.asue24.financetrackerbackend.services.account.AccountService;
import org.asue24.financetrackerbackend.services.account.AccountServiceImpl;
import org.asue24.financetrackerbackend.services.caching.RedisService;
import org.asue24.financetrackerbackend.services.transaction.TransactionServiceImpl;
import org.asue24.financetrackerbackend.services.transaction.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    User user = new User(22L,"lahcen", "lahcenhhh", "ahtat", "hereweare");
    Account account = new Account(1L, "lahcen", 22.2, user);
    private Transaction trans = new Transaction(22L,1.22,LocalDate.now(),"desc",TransactionType.EXPENSE,account);

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private RedisService redisService;
    @Mock
    private AccountServiceImpl accountService;

    /// ///////////////////////////// happy path
    @Test
    void createTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(trans);
        when(accountService.getAccountByAccountId(1L)).thenReturn(account);
        Integer senderId = 1;
        var result = transactionService.createTransaction(trans,senderId,null);
        assertNotNull(result);
        assertEquals(trans, result);
    }

    @Test
    void deleteTransaction() {
        when (transactionRepository.existsById(trans.getId())).thenReturn(true);
        var result =transactionService.deleteTransaction(trans.getId());
        assertNotNull(result);
        assertEquals(true, result);

    }

    @Test
    void updateTransaction() {
        when(transactionRepository.findById(trans.getId())).thenReturn(Optional.of(trans));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(trans);
        var result = transactionService.updateTransaction(trans.getId(), trans);
        assertNotNull(result);
        assertEquals(trans, result);
    }

    @Test
    void getTransactionById() {
        when(transactionRepository.findById(trans.getId())).thenReturn(Optional.of(trans));
        var result = transactionService.getTransaction(trans.getId());
        assertNotNull(result);
        assertEquals(trans, result);
    }

    @Test
    void getAllTransactions() {
        var transactions= List.of(trans);
        when(transactionRepository.findAll()).thenReturn(transactions);
        var result = transactionService.getAllTransactions();
        assertNotNull(result);
        assertEquals(transactions, result);
    }
    ///////////// Edge Cases
}