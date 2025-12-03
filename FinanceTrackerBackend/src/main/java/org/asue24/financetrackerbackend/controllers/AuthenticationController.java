package org.asue24.financetrackerbackend.controllers;


import jakarta.validation.Valid;
import org.asue24.financetrackerbackend.dto.AuthenticationResponse;
import org.asue24.financetrackerbackend.dto.CreateUserDto;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.security.JwtConfig;
import org.asue24.financetrackerbackend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> Login(@Valid @RequestBody UserRequestDto userRequestDto) {
        if (userRequestDto == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var result = userService.AuthenticateUser(userRequestDto);
        return new ResponseEntity<>(new AuthenticationResponse( jwtConfig.generateToken(result.getEmail()), result.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> Signup(@Valid @RequestBody CreateUserDto userRequestDto) {
        if (userRequestDto == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var registrationResult = userService.createUser(userRequestDto);
        if (registrationResult == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        return new ResponseEntity("user created successfully", HttpStatus.OK);

    }
}
