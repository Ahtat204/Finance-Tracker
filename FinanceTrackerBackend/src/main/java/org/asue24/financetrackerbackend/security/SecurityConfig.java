package org.asue24.financetrackerbackend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.asue24.financetrackerbackend.services.user.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/** * Main security configuration class for the application.
 * This class defines the security filter chain, password encoding mechanisms,
 * and authorization rules for different API endpoints.
 * It enables Web Security and configures a stateless session policy. */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService) {

        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    /** * Defines the password encoder bean used across the application.
     *  @return A {@link BCryptPasswordEncoder} instance for secure password hashing. */
    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** * Configures the {@link SecurityFilterChain} to define which HTTP requests are protected.
     *  This method handles: * <ul> * <li>Disabling CSRF and HTTP Basic authentication.
     *  </li> * <li>Permitting all requests to authentication endpoints.</li> * <li>Restricting user management endpoints to ADMIN roles.
     *  </li> * <li>Setting session management to STATELESS.
     *  </li> * <li>Registering the custom {@link JwtFilter}.
     *  </li> * </ul> * * @param http The HttpSecurity object to configure.
     *  * @return The built SecurityFilterChain. * @throws Exception If an error occurs during configuration. */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/user/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    /** * Configures and provides the {@link AuthenticationManager} bean.
     *  This manager uses the custom {@link UserDetailsService} and the defined
     *  {@link BCryptPasswordEncoder} to validate user credentials.
     *  @param http The HttpSecurity object. * @return The configured AuthenticationManager.
     *  @throws Exception If the configuration fails. */
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        return authenticationManagerBuilder.build();
    }

}