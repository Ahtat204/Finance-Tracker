package org.asue24.financetrackerbackend;

import org.asue24.enums.TransactionType;
import org.asue24.financetrackerbackend.dto.AuthenticationResponse;
import org.asue24.financetrackerbackend.dto.CreateUserDto;
import org.asue24.financetrackerbackend.dto.TransactionBody;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.repositories.AccountRepository;
import org.asue24.financetrackerbackend.repositories.TransactionRepository;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.asue24.financetrackerbackend.services.caching.CachingService;
import org.asue24.financetrackerbackend.services.caching.RedisService;
import org.asue24.financetrackerbackend.services.jwt.JwtService;
import org.asue24.financetrackerbackend.services.transaction.TransactionService;
import org.asue24.financetrackerbackend.services.user.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest extends TestDependencies {

    @Autowired
    public RedisService<Transaction> redisService;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private CreateUserDto createUserDto;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CachingService cachingService;
    private Logger logger = LoggerFactory.getLogger(TransactionIntegrationTest.class);

    @AfterAll
    public static void stop() {
        redis.stop();
        postgresContainer.stop();
    }

    @AfterEach
    public void tearDown() {

    }

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        createUserDto = new CreateUserDto("lahcen", "lhdh", "lahcen28ahtat@gmail", "1234password");
    }

    /// //////////Redis Tests///////////////////

    @Test
    public void GetAndSetTest() {
        var Trans = new Transaction(1L, 22.3);
        redisService.put(Trans.getId().toString(), Trans);
        assertNotNull(redisService.get(Trans.getId().toString()));
        assertEquals(Trans.getId(), redisService.get(Trans.getId().toString()).getId());
        assertEquals(Trans.getAmount(), redisService.get(Trans.getId().toString()).getAmount());
    }
    /// ///////Postgres Tests///////////////////
    @Test
    public void GetAllUsers_Test() {
        var User1 = new User("lahcen", "John", "Doki22@gmail.com");
        var User2 = new User("julien", "Jane", "lahcen@gmail.com");
        restTemplate.postForObject("/users", User1, User.class);
        restTemplate.postForObject("/users", User2, User.class);
        var response = restTemplate.getForEntity("/users", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED); //tests that users is a protected endpoint
    }
    @Test
    public void RegisterUserTest() {
        var User = new User("lahcen", "John", "Do78eg@gmail.com", "lahce5452");
        var RegisterRequest = new CreateUserDto(User.getFirstname(), User.getLastname(), User.getEmail(), User.getPassword());
        var response = restTemplate.postForObject("/api/auth/signup", RegisterRequest, String.class);
        assertThat(response).isNotNull();
    }
    @Test
    public void GetUserByEmailTest_ShouldReturnNonNullUser() {
        var User = new User("lahcen", "John", "Dahtao@gmail.com", "lahce5452");
        var request = userService.createUser(new CreateUserDto("test1","test2","ahtat652@gmail.com","hihih22"));
        var user3 = userService.getUserByEmail(request.getEmail());
        assertThat(user3.getEmail()).isNotNull();
        assertThat(user3.getPassword()).isNotNull();
        assertThat(user3.getPassword()).isNotEqualTo(request.getPassword());
        assertThat(user3.getEmail()).isEqualTo(request.getEmail());
    }
    @Test
    public void GetUserByEmailTest_ShouldReturnNullUser() {
        var request = userService.createUser(new CreateUserDto("aht","lhes","ah33st@gmail.com","lahce5452"));
        var user3 = userService.getUserByEmail("someemail@gmail.com");
        assertThat(user3).isNull();
    }
    @Test
    public void GetUserByIdTest_ShouldReturnNonNullUser() {
        var request = userRepository.save(new User("lahcen", "John", "Do@gmail.com", "lahce5452"));
        var Id = request.getId();
        var result = userService.getUserById(Id);
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isNotNull();
        assertThat(result.getPassword()).isNotNull();
        assertThat(result.getPassword()).isEqualTo(request.getPassword());
    }
    @Test
    public void AuthenticateNonExistingUserTest_ShouldReturnNullUser() {
        var result = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahtatn28ahtat@gmail", "1234password"), AuthenticationResponse.class);
        assertThat(result).isNull();
    }

    @Test
    public void RegisterThenLoginUserTest() {
        var user = restTemplate.postForObject("/api/auth/signup", createUserDto, String.class);
        var result = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahcen28ahtat@gmail", "1234password"), AuthenticationResponse.class);
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(createUserDto.email());
    }

    @Test
    public void testTransactionCreation() {
        var user2 = userRepository.findUserById(1000L).get();
        var account = accountRepository.findAccountByUserId(user2.getId()).get();
        var transaction = new Transaction(10.1, LocalDate.now(), "testing Transaction", TransactionType.EXPENSE, account);
        var trans = transactionRepository.save(transaction);
        Assertions.assertNotNull(trans);
    }

    @Test
    public void CreateExpenseTransactionTest() {
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
        var createUserDto = new CreateUserDto("lahcen", "lhdh", "lahcen290ahtat@gmail", "1234password");
        var user = restTemplate.postForObject("/api/auth/signup", createUserDto, String.class);
        var result1 = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahcen290ahtat@gmail", "1234password"), AuthenticationResponse.class);
        Assertions.assertNotNull(result1);
        var token = result1.jwtToken();
        var claims = jwtService.extractAllClaims(token);
        var Id = claims.get("id").toString();
        var accountRequest = new Account("lahcen", 222.22, new User(Long.parseLong(Id)));
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Account> accountEntity = new HttpEntity<>(accountRequest, headers);
        ResponseEntity<Account> response = restTemplate.postForEntity("/api/accounts", accountEntity, Account.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        var body = response.getBody();
        var balance = body.getbalance();
        var trans = new Transaction(20.0, LocalDate.now(), "testing Transaction", TransactionType.EXPENSE, new Account(body.getId()));
        Integer accountId = Integer.valueOf(Math.toIntExact(body.getId()));
        var transbody = new TransactionBody(trans, Integer.valueOf(accountId), Optional.empty());
        HttpEntity<TransactionBody> transactionEntity = new HttpEntity<>(transbody, headers);
        ResponseEntity<Transaction> transactionResponse = restTemplate.postForEntity("/api/transactions", transactionEntity, Transaction.class);
        Assertions.assertNotNull(transactionResponse);
        Assertions.assertEquals(HttpStatus.CREATED, transactionResponse.getStatusCode());
        Assertions.assertNotNull(transactionResponse.getBody());
        var accountBalance = accountRepository.getAccountById(body.getId()).get().getbalance();
        Assertions.assertEquals(balance, accountBalance + trans.getAmount());
    }

    @Test
    public void CreateTransferTransactionTest() {
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
        var createUserDto1 = new CreateUserDto("lahcen", "lhdh", "lahcen23ahtat@gmail", "1234password");
        var createUserDto2 = new CreateUserDto("lahcen", "lhdh", "lahcen38ahtat@gmail", "1234password");

        var user1 = restTemplate.postForObject("/api/auth/signup", createUserDto1, String.class);
        var user2 = restTemplate.postForObject("/api/auth/signup", createUserDto2, String.class);

        var result1 = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahcen23ahtat@gmail", "1234password"), AuthenticationResponse.class);
        var result2 = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahcen38ahtat@gmail", "1234password"), AuthenticationResponse.class);

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);

        var token1 = result1.jwtToken();
        var token2 = result2.jwtToken();

        var claims1 = jwtService.extractAllClaims(token1);
        var claims2 = jwtService.extractAllClaims(token2);
        Assertions.assertNotNull(claims1);
        var Id1 = claims1.get("id").toString();
        var Id2 = claims2.get("id").toString();

        var accountRequest1 = new Account("lahcen", 222.22, new User(Long.parseLong(Id1)));
        var accountRequest2 = new Account("lahcen", 222.22, new User(Long.parseLong(Id2)));

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token1);

        HttpEntity<Account> accountEntity1 = new HttpEntity<>(accountRequest1, headers);
        HttpEntity<Account> accountEntity2 = new HttpEntity<>(accountRequest2, headers);

        ResponseEntity<Account> response1 = restTemplate.postForEntity("/api/accounts", accountEntity1, Account.class);
        ResponseEntity<Account> response2 = restTemplate.postForEntity("/api/accounts", accountEntity2, Account.class);

        Assertions.assertNotNull(response1);
        Assertions.assertNotNull(response2);

        Assertions.assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, response2.getStatusCode());

        Assertions.assertNotNull(response1.getBody());
        Assertions.assertNotNull(response2.getBody());

        var body1 = response1.getBody();
        var body2 = response2.getBody();

        var balance1 = body1.getbalance();
        var balance2 = body2.getbalance();

        var trans = new Transaction(20.0, LocalDate.now(), "testing Transaction", TransactionType.TRANSFER, new Account());

        Integer accountId1 = Integer.valueOf(Math.toIntExact(body1.getId()));
        Integer accountId2 = Integer.valueOf(Math.toIntExact(body2.getId()));
        var transbody = new TransactionBody(trans, Integer.valueOf(accountId1), Optional.of(accountId2));
        HttpEntity<TransactionBody> transactionEntity = new HttpEntity<>(transbody, headers);
        ResponseEntity<Transaction> transactionResponse = restTemplate.postForEntity("/api/transactions", transactionEntity, Transaction.class);
        Assertions.assertNotNull(transactionResponse);
        Assertions.assertEquals(HttpStatus.CREATED, transactionResponse.getStatusCode());
        Assertions.assertNotNull(transactionResponse.getBody());
        var accountbalance1 = accountRepository.getAccountById(body1.getId()).get().getbalance();
        var accountbalance2 = accountRepository.getAccountById(body2.getId()).get().getbalance();
        Assertions.assertEquals(balance1 - trans.getAmount(), accountbalance1);
        Assertions.assertEquals(accountbalance2, balance2 + trans.getAmount());
    }
}
