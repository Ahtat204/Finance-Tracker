package org.asue24.financetrackerfrontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.asue24.financetrackerfrontend.model.dto.login.LoginRequest;
import org.asue24.financetrackerfrontend.model.dto.login.LoginResponse;
import org.asue24.financetrackerfrontend.services.AuthenticationService;

import java.net.MalformedURLException;

import static org.asue24.financetrackerfrontend.Env.url;

public class AuthController {
    private final AuthenticationService authenticationService = new AuthenticationService();
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button submitButton;

    public AuthController() throws MalformedURLException {
    }

    @FXML
    public LoginResponse Login() {
        var loginRequest = new LoginRequest(username.getText(), password.getText());
        return authenticationService.Authenticate(loginRequest, url);
    }
}
