package org.asue24.financetrackerfrontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.asue24.financetrackerfrontend.model.dto.LoginRequest;
import org.asue24.financetrackerfrontend.model.dto.LoginResponse;
import org.asue24.financetrackerfrontend.model.dto.RegisterReponse;
import org.asue24.financetrackerfrontend.model.dto.RegisterRequest;
import org.asue24.financetrackerfrontend.services.AuthenticationRepository;

import static org.asue24.financetrackerfrontend.Env.url;

public class AuthController {
    private final AuthenticationRepository authenticationRepository = new AuthenticationRepository();
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button submitButton;
    @FXML
    private Hyperlink register;
    @FXML
    public LoginResponse Login() {
        var loginRequest = new LoginRequest(username.getText(), password.getText());
        return authenticationRepository.login(loginRequest, url);
    }
    @FXML
    public RegisterReponse register(){
        var registerRequest = new RegisterRequest(username.getText(), password.getText());
        return authenticationRepository.register(registerRequest, url);
    }

    @FXML
    public void navigateToRegister() {

    }

}
