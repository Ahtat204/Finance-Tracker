package org.asue24.financetrackerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
public class FinanceTrackerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceTrackerBackendApplication.class, args);
    }

}
