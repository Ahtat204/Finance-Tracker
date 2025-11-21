package org.asue24.financetrackerbackend.controllers;


import jakarta.validation.Valid;
import org.asue24.financetrackerbackend.dto.AuthenticationResponse;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public ResponseEntity<AuthenticationResponse> Login(@Valid  @RequestBody UserRequestDto userRequestDto) {
        var authenticationResult=userService.getUser(userRequestDto);
        if(authenticationResult==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(authenticationResult);
    }
    @PostMapping("/signup")
    public ResponseEntity<String> Signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok().body("success");
    }
}
