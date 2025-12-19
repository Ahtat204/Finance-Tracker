package org.asue24.financetrackerbackend.integrationtests.postgres.services;


import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.integrationtests.postgres.PostgresTest;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
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
    private UserRepository userRepository;
    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
    //passed successfully
    @Test
    public void GetAllUsers_Test()
    {
        var User1=new User("lahcen","John","Do@gmail.com");
        var User2=new User("julien","Jane","lahcen@gmail.com");
        restTemplate.postForObject("/users", User1, User.class);
        restTemplate.postForObject("/users", User2, User.class);
        var response=restTemplate.getForEntity("/users", String.class);
        var responseBody=response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED); //tests that users is a protected endpoint
    }
}
