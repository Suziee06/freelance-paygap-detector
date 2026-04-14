package com.freelancedetector.ui.controller;

import com.freelancedetector.ui.utils.SceneManager;
import javafx.fxml.FXML;

public class ProjectDetailController {

    @FXML
    public void initialize() {
        // Load details for the currently selected project here
    }

    @FXML
    public void goBack() {
        SceneManager.switchScene("/fxml/ProjectListScreen.fxml", "My Projects");
    }
    
    @FXML
    public void addPayment() {
        System.out.println("Add Payment Clicked");
    }
    
    @FXML
    public void addWorkLog() {
        System.out.println("Add Work Log Clicked");
    }
}
