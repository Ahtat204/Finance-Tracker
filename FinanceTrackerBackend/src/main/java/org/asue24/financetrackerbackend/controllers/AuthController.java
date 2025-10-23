package org.asue24.financetrackerbackend.controllers;

import org.asue24.financetrackerbackend.services.user.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("api/auth")
public class AuthController {
private UserService userService;



}
