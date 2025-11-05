package org.asue24.financetrackerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@SpringBootApplication
public class FinanceTrackerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceTrackerBackendApplication.class, args);
    }

}
