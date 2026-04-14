package com.freelancedetector.ui.controller;

import com.freelancedetector.ui.utils.ApiClient;
import com.freelancedetector.ui.utils.SceneManager;
import com.freelancedetector.ui.utils.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator loadingSpinner;

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (loadingSpinner != null) loadingSpinner.setVisible(true);
        if (loginButton != null) loginButton.setDisable(true);

        Map<String, String> creds = Map.of("email", email, "password", password);

        ApiClient.post("/users/login", creds).thenAccept(response -> {
            Platform.runLater(() -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> user = mapper.readValue(response, new TypeReference<Map<String,Object>>(){});
                    
                    SessionManager.getInstance().setSession(
                        Long.valueOf(user.get("userId").toString()),
                        user.get("name").toString(),
                        user.get("gender").toString()
                    );
                    
                    SceneManager.switchScene("/fxml/DashboardScreen.fxml", "Dashboard");
                } catch (Exception e) {
                    showAlert("Parsing Error", "Invalid response from server.");
                }
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> {
                if (loadingSpinner != null) loadingSpinner.setVisible(false);
                if (loginButton != null) loginButton.setDisable(false);
                showAlert("Login Failed", ex.getMessage());
            });
            return null;
        });
    }

    @FXML
    public void goToRegister() {
        SceneManager.switchScene("/fxml/RegisterScreen.fxml", "Register");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
