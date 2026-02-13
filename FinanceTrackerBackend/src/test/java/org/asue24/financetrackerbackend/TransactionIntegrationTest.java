package org.asue24.financetrackerbackend;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.asue24.enums.TransactionType;
import org.asue24.financetrackerbackend.dto.AuthenticationResponse;
import org.asue24.financetrackerbackend.dto.CreateUserDto;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.repositories.AccountRepository;
import org.asue24.financetrackerbackend.repositories.TransactionRepository;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.asue24.financetrackerbackend.services.caching.CachingService;
import org.asue24.financetrackerbackend.services.jwt.JwtService;
import org.asue24.financetrackerbackend.services.transaction.TransactionService;
import org.junit.jupiter.api.AfterAll;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

//since Transaction cannot be created without an account , which cannot be created without a user , we will rely on the fact that the database already contains account(s) and user(s)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionIntegrationTest extends TestDependencies {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CachingService cachingService;
    private Logger logger=  LoggerFactory.getLogger(TransactionIntegrationTest.class);


    @AfterAll
    public static void tearDown() {
        postgresContainer.stop();
        redis.stop();
    }



    @Test
    public void testTransactionCreation() {
        var user2 = userRepository.findUserById(2L).get();
        var account1 = accountRepository.getReferenceById(user2.getId());
        var transaction = new Transaction(10.1, LocalDate.now(), "testing Transaction", TransactionType.EXPENSE, account1);
        var trans = transactionRepository.save(transaction);
        Assertions.assertNotNull(trans);
    }

    @Test
    public void CreateTransactionTest() {
          /*
        var user1 = userRepository.findUserById(1L);
        var user2 = userRepository.findUserById(2L);
        if (!user1.isPresent() && !user2.isPresent()) return;
        var value1 = user1.get();
        var value2 = user2.get();
        var account1 = accountRepository.getById(value1.getId());
        var account2 = accountRepository.getById(value2.getId());
        var trans = new Transaction(10.1, LocalDate.now(), "testing Transaction", TransactionType.TRANSFER, account1);
        var result = transactionService.createTransaction(trans, 2, Optional.of(3));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(account1.getbalance(), account1.getbalance() + trans.getAmount());
        Assertions.assertEquals(account2.getbalance(), account2.getbalance() - trans.getAmount());

         */
        var createUserDto = new CreateUserDto("lahcen", "lhdh", "lahcen28ahtat@gmail", "1234password");
        var user = restTemplate.postForObject("/api/auth/signup", createUserDto, String.class);
        var result1 = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahcen28ahtat@gmail", "1234password"), AuthenticationResponse.class);
        var token=result1.jwtToken();
        var claims=jwtService.extractAllClaims(token);
        var Id=claims.get("id").toString();
        var account=new Account("lahcen",222.22,new User(Long.parseLong(Id)));
        var result2=restTemplate.postForObject("/api/accounts", account, Account.class);
        var trans=new Transaction(20.0, LocalDate.now(), "testing Transaction", TransactionType.EXPENSE, new Account(account.getId()));
        var transaction=restTemplate.postForObject("/api/transactions", trans, Transaction.class);
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotNull(transaction);
    }
}
