package org.asue24.financetrackerfrontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.asue24.financetrackerfrontend.model.dto.login.LoginResponse;
import org.asue24.financetrackerfrontend.services.AuthenticationService;

import java.io.IOException;
import java.net.URISyntaxException;

public class AuthController {
    private final AuthenticationService authenticationService = new AuthenticationService();
    String url=System.getenv("URL");
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button submitButton;

    @FXML
    public LoginResponse Login(String email, String password) throws IOException, InterruptedException, URISyntaxException {

        return null;
    }
}
