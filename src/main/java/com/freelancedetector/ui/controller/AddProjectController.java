package com.freelancedetector.ui.controller;

import com.freelancedetector.ui.utils.ApiClient;
import com.freelancedetector.ui.utils.SceneManager;
import com.freelancedetector.ui.utils.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.Map;

public class AddProjectController {

    @FXML private TextField titleField;
    @FXML private TextField clientNameField;
    @FXML private ComboBox<String> typeBox;
    @FXML private TextField amountField;
    @FXML private TextField hoursField;
    @FXML private DatePicker deadlinePicker;
    @FXML private Button submitButton;
    @FXML private ProgressIndicator loadingSpinner;

    @FXML
    public void submitProject() {
        if (titleField.getText().isEmpty() || typeBox.getValue() == null || amountField.getText().isEmpty() || hoursField.getText().isEmpty()) {
            showAlert("Required", "Please fill in all core fields.");
            return;
        }

        try {
            Double amount = Double.parseDouble(amountField.getText());
            Double hours = Double.parseDouble(hoursField.getText());
            
            loadingSpinner.setVisible(true);
            submitButton.setDisable(true);

            Map<String, Object> payload = Map.of(
                "userId", SessionManager.getInstance().getLoggedInUserId(),
                "title", titleField.getText(),
                "projectType", typeBox.getValue(),
                "agreedAmount", amount,
                "agreedHours", hours,
                "deadline", deadlinePicker.getValue() != null ? deadlinePicker.getValue().toString() : LocalDate.now().plusMonths(1).toString(),
                "status", "ACTIVE"
            );

            // Need to wrap in an expected nested structure depending on Phase 3 design
            ApiClient.post("/projects", payload).thenAccept(response -> {
                Platform.runLater(() -> {
                    SceneManager.switchScene("/fxml/ProjectListScreen.fxml", "My Projects");
                });
            }).exceptionally(ex -> {
                Platform.runLater(() -> {
                    loadingSpinner.setVisible(false);
                    submitButton.setDisable(false);
                    showAlert("Failed", ex.getMessage());
                });
                return null;
            });

        } catch (NumberFormatException e) {
            showAlert("Format Error", "Amount and Hours must be valid numbers.");
        }
    }

    @FXML
    public void goBack() {
        SceneManager.switchScene("/fxml/DashboardScreen.fxml", "Dashboard");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
