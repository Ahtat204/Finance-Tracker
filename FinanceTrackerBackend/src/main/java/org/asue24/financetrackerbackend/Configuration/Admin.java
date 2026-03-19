package org.asue24.financetrackerbackend.Configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@Setter
@ConfigurationProperties(prefix = "admin")
public class Admin {
    @Value("${ADMIN_EMAIL}")
    private String email;
    @Value("${ADMIN_PASSWORD}")
    private String password;
}
