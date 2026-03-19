package org.asue24.financetrackerbackend.Configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "admin")
public class Admin {
    @Value("${admin.email}")
    private String email;
    @Value("${admin.password}")
    private String password;
}
