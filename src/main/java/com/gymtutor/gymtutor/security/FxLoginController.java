package com.gymtutor.gymtutor.security;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FxLoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @Autowired
    private FxLoginService loginService;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String senha = passwordField.getText();

        if (loginService.authenticate(email, senha)) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Login efetuado com sucesso!");
            // Avance para tela principal
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Email ou senha inválidos.");
        }
    }
}