package org.asue24.financetrackerbackend;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

@EnableCaching
@SpringBootApplication
public class FinanceTrackerBackendApplication {


    public static void main(String[] args) {
        ConfigurableEnvironment environment = new StandardEnvironment();
        SpringApplication.run(FinanceTrackerBackendApplication.class, args);
        var ds = new HikariDataSource();
        System.out.println(ds.getJdbcUrl() + "" + ds.getUsername() + "" + ds.getPassword() + "${" + environment.getActiveProfiles()[0] + "}");

    }

}
