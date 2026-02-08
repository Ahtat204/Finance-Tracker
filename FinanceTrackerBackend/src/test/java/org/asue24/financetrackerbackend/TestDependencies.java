package org.asue24.financetrackerbackend;

import com.redis.testcontainers.RedisContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers()

public class TestDependencies {
    @Container
    protected static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withExposedPorts(5432).withAccessToHost(true)
                    .withUsername("postgres")
                    .withPassword("test")
                    .withClasspathResourceMapping("init.sql",
                            "/docker-entrypoint-initdb.d/init.sql",
                            BindMode.READ_ONLY);
    @Container
    static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", TestDependencies::getFormattedConnectionString);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    private static String getFormattedConnectionString() {
        return String.format("jdbc:postgresql://%s:%s/financetracker",
                postgresContainer.getHost(),
                postgresContainer.getFirstMappedPort());
    }
}
