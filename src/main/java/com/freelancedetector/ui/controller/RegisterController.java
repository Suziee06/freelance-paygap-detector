package com.freelancedetector.ui.controller;

import com.freelancedetector.ui.utils.ApiClient;
import com.freelancedetector.ui.utils.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Map;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> genderBox;
    @FXML private Button registerButton;
    @FXML private ProgressIndicator loadingSpinner;

    @FXML
    public void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String gender = genderBox.getValue();

        if (name == null || email == null || password == null || gender == null) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (loadingSpinner != null) loadingSpinner.setVisible(true);
        if (registerButton != null) registerButton.setDisable(true);

        Map<String, String> payload = Map.of(
            "name", name,
            "email", email,
            "password", password,
            "gender", gender
        );

        ApiClient.post("/users/register", payload).thenAccept(response -> {
            Platform.runLater(() -> {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setContentText("Registration successful! Please login.");
                success.showAndWait();
                SceneManager.switchScene("/fxml/LoginScreen.fxml", "Login");
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> {
                if (loadingSpinner != null) loadingSpinner.setVisible(false);
                if (registerButton != null) registerButton.setDisable(false);
                showAlert("Registration Failed", ex.getMessage());
            });
            return null;
        });
    }

    @FXML
    public void goToLogin() {
        SceneManager.switchScene("/fxml/LoginScreen.fxml", "Login");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
