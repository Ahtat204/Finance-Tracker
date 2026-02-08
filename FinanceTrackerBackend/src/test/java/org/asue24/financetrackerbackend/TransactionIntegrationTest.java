package org.asue24.financetrackerbackend;

import org.asue24.enums.TransactionType;
import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.repositories.AccountRepository;
import org.asue24.financetrackerbackend.repositories.TransactionRepository;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.asue24.financetrackerbackend.services.caching.CachingService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@SpringBootTest(classes = FinanceTrackerBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionIntegrationTest extends TestDependencies {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CachingService cachingService;

    private User user = new User( "lahcen", "ahtat204", "lahcen@asue24.org");
    private Account account = new Account("", 100.0, null);
    private Transaction transaction = new Transaction(10.1, LocalDate.now(), "testing Transaction", TransactionType.EXPENSE,account);


    @AfterAll
    public static void tearDown() {
        postgresContainer.stop();
        redis.stop();
    }

    @Test
    public void testTransactionCreation() {
        userRepository.save(user);
        accountRepository.save(account);
        transactionRepository.save(transaction);
        var transaction1 = transactionRepository.findById(transaction.getId()).get();
        Assertions.assertEquals(transaction.getId(), transaction1.getId());
        Assertions.assertEquals(transaction.getAmount(), transaction1.getAmount());
    }
}
