package org.asue24.financetrackerbackend.security;

import org.asue24.financetrackerbackend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;


    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }
}
