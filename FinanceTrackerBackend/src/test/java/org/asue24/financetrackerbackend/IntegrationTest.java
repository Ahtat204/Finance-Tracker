package org.asue24.financetrackerbackend;

import org.asue24.financetrackerbackend.dto.AuthenticationResponse;
import org.asue24.financetrackerbackend.dto.CreateUserDto;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.asue24.financetrackerbackend.services.caching.RedisService;
import org.asue24.financetrackerbackend.services.user.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

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

    @AfterAll
    public static void stop() {
        redis.stop();
        postgresContainer.stop();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
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
        var User1 = new User("lahcen", "John", "Do@gmail.com");
        var User2 = new User("julien", "Jane", "lahcen@gmail.com");
        restTemplate.postForObject("/users", User1, User.class);
        restTemplate.postForObject("/users", User2, User.class);
        var response = restTemplate.getForEntity("/users", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED); //tests that users is a protected endpoint
    }
    @Test
    public void RegisterUserTest() {
        var User = new User("lahcen", "John", "Do@gmail.com", "lahce5452");
        var RegisterRequest = new CreateUserDto(User.getFirstname(), User.getLastname(), User.getEmail(), User.getPassword());
        var response = restTemplate.postForObject("/api/auth/signup", RegisterRequest, String.class);
        assertThat(response).isNotNull();
    }
    @Test
    public void GetUserByEmailTest_ShouldReturnNonNullUser() {
        var User = new User("lahcen", "John", "Do@gmail.com", "lahce5452");
        var request = userService.createUser(createUserDto);
        var user3 = userService.getUserByEmail(request.getEmail());
        assertThat(user3.getEmail()).isNotNull();
        assertThat(user3.getPassword()).isNotNull();
        assertThat(user3.getPassword()).isNotEqualTo(request.getPassword());
        assertThat(user3.getEmail()).isEqualTo(request.getEmail());
    }
    @Test
    public void GetUserByEmailTest_ShouldReturnNullUser() {
        var request = userService.createUser(createUserDto);
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
        var result = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahcen28ahtat@gmail", "1234password"), AuthenticationResponse.class);
        assertThat(result).isNull();
    }

    @Test
    public void RegisterThenLoginUserTest() {
        var user = restTemplate.postForObject("/api/auth/signup", createUserDto, String.class);
        var result = restTemplate.postForObject("/api/auth/login", new UserRequestDto("lahcen28ahtat@gmail", "1234password"), AuthenticationResponse.class);
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(createUserDto.email());
    }
}
