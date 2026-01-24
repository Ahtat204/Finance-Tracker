package org.asue24.financetrackerbackend.integrationtest;

import com.redis.testcontainers.RedisContainer;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.asue24.financetrackerbackend.services.caching.RedisService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SpringBootIntegrationTest extends PostgresTest{

    @Container
     static RedisContainer redis= new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
    @Autowired
    public RedisService<Transaction> redisService;
    @BeforeAll
    public static void init() {
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString());
    }
    @AfterAll
    public static void stop() {
        redis.stop();
    }
    @Test
    public void GetAndSetTest() {
        var Trans = new Transaction(1L, 22.3);
        redisService.put(Trans.getId().toString(), Trans);
        Assertions.assertNotNull(redisService.get(Trans.getId().toString()));
        Assertions.assertEquals(Trans.getId(), redisService.get(Trans.getId().toString()).getId());
        Assertions.assertEquals(Trans.getAmount(), redisService.get(Trans.getId().toString()).getAmount());
    }
}
