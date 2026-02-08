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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

//since Transaction cannot be created without an account , which cannot be created without a user , we will rely on the fact that the database already contains account(s) and user(s)

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

    private User user1 ;
    private User user2 ;
    private Account account1 = new Account("", 100.0, null);
    private Transaction transaction = new Transaction(10.1, LocalDate.now(), "testing Transaction", TransactionType.EXPENSE,account1);


    @BeforeEach
    public void setUp() {

    }
    @AfterAll
    public static void tearDown() {
        postgresContainer.stop();
        redis.stop();
    }

    @Test
    public void testTransactionCreation() {
        user1=userRepository.findUserById(1L).get();
        user2=userRepository.findUserById(2L).get();
        account1=accountRepository.getReferenceById(user1.getId());
        account1=accountRepository.getReferenceById(user2.getId());
      var trans = transactionRepository.save(transaction);
      Assertions.assertNotNull(trans);

    }
}
