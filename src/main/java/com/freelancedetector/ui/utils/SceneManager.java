package com.freelancedetector.ui.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
