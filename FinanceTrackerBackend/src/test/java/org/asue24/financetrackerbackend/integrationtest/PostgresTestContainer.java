package org.asue24.financetrackerbackend.integrationtest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class PostgresTestContainer {
    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>
            (" postgres:13.1-alpine")
            .withDatabaseName("financetracker")
            .withExposedPorts(5432);

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

    }
}
