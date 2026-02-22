package org.asue24.financetrackerfrontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.asue24.financetrackerfrontend.model.dto.login.LoginRequest;
import org.asue24.financetrackerfrontend.model.dto.login.LoginResponse;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthController {
    String url=System.getenv("URL");
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button submitButton;

    @FXML
    public LoginResponse Login(LoginRequest lRequest) throws IOException, InterruptedException, URISyntaxException {
        var httpClient=HttpClient.newHttpClient();
        var bodyJson=new Gson().toJson(lRequest);
        var request = HttpRequest.newBuilder(new URI(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                .build();
        populateRequestFields(request, bodyJson);
        HttpResponse<String> response=httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
