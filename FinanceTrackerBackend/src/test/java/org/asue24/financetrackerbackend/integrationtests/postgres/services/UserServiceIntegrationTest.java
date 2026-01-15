package org.asue24.financetrackerbackend.integrationtests.postgres.services;


import org.asue24.financetrackerbackend.dto.AuthenticationResponse;
import org.asue24.financetrackerbackend.dto.CreateUserDto;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.integrationtests.postgres.PostgresTest;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.asue24.financetrackerbackend.services.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest extends PostgresTest {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private CreateUserDto createUserDto;

    @BeforeEach
     void setUpBeforeClass() throws Exception {
        createUserDto=new CreateUserDto("lahcen","lhdh","lhce@gmail.com","password");
    }
    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    //passed successfully
    @Test
    public void GetAllUsers_Test() {
        var User1 = new User("lahcen", "John", "Do@gmail.com");
        var User2 = new User("julien", "Jane", "lahcen@gmail.com");
        restTemplate.postForObject("/users", User1, User.class);
        restTemplate.postForObject("/users", User2, User.class);
        var response = restTemplate.getForEntity("/users", String.class);
        var responseBody = response.getBody();
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
        var request=userService.createUser(createUserDto);
        var user3=userService.getUserByEmail(request.getEmail());
        assertThat(user3.getEmail()).isNotNull();
        assertThat(user3.getPassword()).isNotNull();
        assertThat(user3.getPassword()).isNotEqualTo(request.getPassword());
        assertThat(user3.getEmail()).isEqualTo(request.getEmail());
    }
}
