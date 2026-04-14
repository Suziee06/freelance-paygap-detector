package com.freelancedetector.ui.controller;

import com.freelancedetector.ui.utils.ApiClient;
import com.freelancedetector.ui.utils.SceneManager;
import com.freelancedetector.ui.utils.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label totalProjectsLabel;
    @FXML private Label pendingPaymentsLabel;
    @FXML private Label avgRiskLabel;
    @FXML private Label criticalProjectsLabel;
    @FXML private ProgressIndicator loadingSpinner;

    @FXML
    public void initialize() {
        if (SessionManager.getInstance().getLoggedInUserName() != null) {
            welcomeLabel.setText("Welcome, " + SessionManager.getInstance().getLoggedInUserName() + "!");
        }
        loadDashboardData();
    }

    private void loadDashboardData() {
        loadingSpinner.setVisible(true);
        Long userId = SessionManager.getInstance().getLoggedInUserId();

        // In a real scenario, you'd fetch from a /api/analytics/summary endpoint.
        // For now, we simulate fetching separate API endpoints or set defaults to avoid crashes.
        Platform.runLater(() -> {
            totalProjectsLabel.setText("0");
            pendingPaymentsLabel.setText("0");
            avgRiskLabel.setText("Low");
            criticalProjectsLabel.setText("0");
            loadingSpinner.setVisible(false);
        });
    }

    @FXML
    public void goToAddProject() {
        SceneManager.switchScene("/fxml/AddProjectScreen.fxml", "Add Project");
    }

    @FXML
    public void goToMyProjects() {
        SceneManager.switchScene("/fxml/ProjectListScreen.fxml", "My Projects");
    }

    @FXML
    public void handleLogout() {
        SessionManager.getInstance().clearSession();
        SceneManager.switchScene("/fxml/LoginScreen.fxml", "Login");
    }
}
