package org.asue24.financetrackerbackend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//since Transaction cannot be created without an account , which cannot be created without a user , we will rely on the fact that the database already contains account(s) and user(s)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionIntegrationTest extends TestDependencies {
    /*
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
    private Logger logger = LoggerFactory.getLogger(TransactionIntegrationTest.class);


    @AfterAll
    public static void tearDown() {
        postgresContainer.stop();
        redis.stop();
    }

    @AfterEach
    public void cleanUp() {

    }

    @Test
    public void testTransactionCreation() {
        var user1=userRepository.save(new User("lahcen","ahtat204","lahcen@asue24.org","lahce33"));
        var user2 = userRepository.findUserById(user1.getId()).get();
        var saveAccount=accountRepository.save(new Account("lahcen",22.2,new User(user1.getId())));
        var account = accountRepository.getReferenceById(user2.getId());
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
    /*
        var createUserDto = new CreateUserDto("lahcen", "lhdh", "lahcen28ahtat@gmail", "1234password");
        var user = restTemplate.postForObject("/api/auth/signup", createUserDto, String.class);
        var result1 = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahcen28ahtat@gmail", "1234password"), AuthenticationResponse.class);
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
    /*
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
    */
}
