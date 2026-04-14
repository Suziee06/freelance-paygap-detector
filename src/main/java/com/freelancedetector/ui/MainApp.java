package com.freelancedetector.ui;

import com.freelancedetector.ui.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setMainStage(primaryStage);
        // Load our freshly generated Login Screen!
        SceneManager.switchScene("/fxml/LoginScreen.fxml", "Login - Freelance Pay Gap Detector");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
