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
        if (userId == null) userId = 1L; // Fallback for debugging

        ApiClient.get("/projects/user/" + userId).thenAccept(response -> {
            Platform.runLater(() -> {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    java.util.List<java.util.Map<String, Object>> projects = mapper.readValue(response, new com.fasterxml.jackson.core.type.TypeReference<java.util.List<java.util.Map<String,Object>>>(){});
                    
                    int total = projects.size();
                    double activePayments = 0.0;
                    int criticalFlags = 0;
                    
                    for (java.util.Map<String, Object> p : projects) {
                        String status = (String) p.getOrDefault("status", "ACTIVE");
                        Object amtObj = p.get("agreedAmount");
                        
                        if ("ACTIVE".equalsIgnoreCase(status) || "PENDING".equalsIgnoreCase(status)) {
                            if (amtObj instanceof Number) {
                                activePayments += ((Number)amtObj).doubleValue();
                            }
                            criticalFlags++; // Simple heuristic
                        }
                    }
                    
                    totalProjectsLabel.setText(String.valueOf(total));
                    pendingPaymentsLabel.setText(String.format("$%.2f", activePayments));
                    avgRiskLabel.setText(total > 0 ? "Medium" : "None");
                    criticalProjectsLabel.setText(String.valueOf(criticalFlags));

                } catch (Exception e) {
                    System.err.println("Dashboard parse error: " + e.getMessage());
                } finally {
                    loadingSpinner.setVisible(false);
                }
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> {
                System.err.println("Failed to load dashboard data: " + ex.getMessage());
                loadingSpinner.setVisible(false);
            });
            return null;
        });
    }

    @FXML
    public void goToAddProject() {
        SceneManager.switchScene("/fxml/AddProjectScreen.fxml", "Add Project");
    }

    @FXML
    public void toggleTheme() {
        com.freelancedetector.ui.utils.ThemeManager.toggleTheme();
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
