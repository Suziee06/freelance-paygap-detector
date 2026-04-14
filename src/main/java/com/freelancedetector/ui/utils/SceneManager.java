package com.freelancedetector.ui.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.io.IOException;

public class SceneManager {
    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchScene(String fxmlFile, String title) {
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlFile));
                
                // Add Interactive Fade-In Page Transition
                FadeTransition fadeIn = new FadeTransition(Duration.millis(350), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

                Scene scene = new Scene(root);
                
                // Boom! Globally applied CSS that listens to Dark/Light mode instantly!
                ThemeManager.applyTheme(scene);
                
                mainStage.setScene(scene);
                mainStage.setTitle(title);
                mainStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
