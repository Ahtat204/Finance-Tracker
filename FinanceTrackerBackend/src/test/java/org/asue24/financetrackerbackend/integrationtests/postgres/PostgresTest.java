package org.asue24.financetrackerbackend.integrationtests.postgres;

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
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.1-alpine"))
                    .withExposedPorts(5432)
                    .withUsername("test")
                    .withPassword("test")
                    .withClasspathResourceMapping("init.sql",
                            "/docker-entrypoint-initdb.d/init.sql",
                            BindMode.READ_ONLY);
    private static String ConnectionString=String.format("jdbc:postgresql://%s:%s/financetracker",
            postgresContainer.getHost(),
            postgresContainer.getFirstMappedPort());
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", ()->PostgresTest.ConnectionString);
        registry.add("spring.datasource.username",postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }


}
