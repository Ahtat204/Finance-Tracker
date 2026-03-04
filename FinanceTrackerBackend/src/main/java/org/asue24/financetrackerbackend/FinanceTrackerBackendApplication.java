package org.asue24.financetrackerbackend;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;


@SpringBootApplication
public class FinanceTrackerBackendApplication {
   static final Logger logger = LoggerFactory.getLogger(FinanceTrackerBackendApplication.class);
    public static void main(String[] args) {
        ConfigurableEnvironment environment = new StandardEnvironment();
        SpringApplication.run(FinanceTrackerBackendApplication.class, args);
        var ds = new HikariDataSource();
        logger.info("active profile is : \t{}", environment.getActiveProfiles()[0]);
    }

}
