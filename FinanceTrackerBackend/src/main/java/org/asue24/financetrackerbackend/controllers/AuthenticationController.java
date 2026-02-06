package org.asue24.financetrackerbackend.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.asue24.financetrackerbackend.dto.AuthenticationResponse;
import org.asue24.financetrackerbackend.dto.CreateUserDto;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.services.jwt.JwtService;
import org.asue24.financetrackerbackend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;


/**
 * REST controller responsible for handling authentication-related requests.
 * Provides endpoints for user registration (signup) and identity verification (login).
 * This controller interacts with the {@link AuthenticationManager} to verify credentials
 * and uses {@link JwtService} to issue security tokens.
 */

@Slf4j
@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user based on email and password.
     * <p>The process follows these steps:</p> <ol> <li>Attempts to authenticate the user using the AuthenticationManager.
     * </li>  <li>Retrieves user details from the database.</li>  <li>Generates a JWT token if authentication is successful.
     * </li>  </ol>  @param userRequestDto DTO containing the user's email and raw password. * @return A {@link ResponseEntity} containing the
     * {@link AuthenticationResponse} (JWT and email) * or an UNAUTHORIZED status if authentication fails.
     */
    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> Login(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            var result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getRawPassword()));
            var userDetails = userService.getUserByEmail(userRequestDto.getEmail());
            log.atWarn().log(result.toString());
            if (userDetails == null) {
                throw new AuthenticationException("User not found");
            }
            // 3. Generate JWT and return 200 OK
            return new ResponseEntity<>(new AuthenticationResponse(jwtService.generateJwt(userDetails.getEmail(),userDetails.getId()), userDetails.getEmail()), HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.atDebug().log(e.getLocalizedMessage() + e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        }
    }

    /**
     * Registers a new user in the system.
     *
     * @param userRequestDto DTO containing registration details for the new user.
     * @return A {@link ResponseEntity} with a success message if created,
     * a BAD_REQUEST status if input is null, or a BAD_GATEWAY status if creation fails.
     */
    @PostMapping("signup")
    public ResponseEntity<String> Signup(@Valid @RequestBody CreateUserDto userRequestDto) {
        if (userRequestDto == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var registrationResult = userService.createUser(userRequestDto);
        if (registrationResult == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        return new ResponseEntity("user created successfully", HttpStatus.OK);

    }
}
