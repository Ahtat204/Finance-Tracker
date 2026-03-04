package org.asue24.financetrackerbackend.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.asue24.financetrackerbackend.services.user.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.asue24.financetrackerbackend.Configuration.GlobalConfigs.bCryptPasswordEncoder;


/** * Main security configuration class for the application.
 * This class defines the security filter chain, password encoding mechanisms,
 * and authorization rules for different API endpoints.
 * It enables Web Security and configures a stateless session policy. */
@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    /** * Configures the {@link SecurityFilterChain} to define which HTTP requests are protected.
     *  This method handles: * <ul> * <li>Disabling CSRF and HTTP Basic authentication.
     *  </li> * <li>Permitting all requests to authentication endpoints.</li> * <li>Restricting user management endpoints to ADMIN roles.
     *  </li> * <li>Setting session management to STATELESS.
     *  </li> * <li>Registering the custom {@link JwtFilter}.
     *  </li> * </ul> * * @param http The HttpSecurity object to configure.
     *  * @return The built SecurityFilterChain. * @throws Exception If an error occurs during configuration. */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(Customizer.withDefaults()).securityMatcher("/actuator/prometheus")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll().requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/user/**").hasRole("ADMIN")
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).denyAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}