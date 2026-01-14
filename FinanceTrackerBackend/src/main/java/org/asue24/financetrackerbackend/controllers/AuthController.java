package org.asue24.financetrackerbackend.controllers;

import org.asue24.financetrackerbackend.entities.User;

import org.asue24.financetrackerbackend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@RestController()
@RequestMapping("api/auth")
public final class AuthController {
/*
    private final UserService userService;
    private final JwtConfig jwtUtil;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthController(UserService userService,
                          JwtConfig jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/signup")
    public Map<String, Object> signUpHandler(@RequestBody User user) {
        var encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user = userService.createUser(user);
        var token = jwtUtil.generateToken(user.getUsername());
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("login")
    public Map<String, Object> loginHandler(@RequestBody User user) {
        try {
            var authInput = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            authenticationManager.authenticate(authInput);
            var token = jwtUtil.generateToken(user.getUsername());
            return Collections.singletonMap("jwt-token", token);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e + "Invalid username or password");
        }
    }
*/
}
