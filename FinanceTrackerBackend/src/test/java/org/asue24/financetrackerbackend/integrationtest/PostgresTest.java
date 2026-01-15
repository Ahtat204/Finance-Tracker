package org.asue24.financetrackerbackend.integrationtest;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers()
public  class PostgresTest {
    @Container
    public static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withExposedPorts(5432)
                    .withUsername("test")
                    .withPassword("test")
                    .withClasspathResourceMapping("init.sql",
                            "/docker-entrypoint-initdb.d/init.sql",
                            BindMode.READ_ONLY);
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", PostgresTest::getFormattedConnectionString);
        registry.add("spring.datasource.username",postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
    private static String getFormattedConnectionString() {
        return String.format("jdbc:postgresql://%s:%s/financetracker",
                postgresContainer.getHost(),
                postgresContainer.getFirstMappedPort());
    }
}