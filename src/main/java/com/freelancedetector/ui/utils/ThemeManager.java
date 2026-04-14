package com.freelancedetector.ui.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;

public class ThemeManager {
    // Default to true (GitHub Dark Mode is arguably the most iconic!)
    private static final BooleanProperty darkMode = new SimpleBooleanProperty(true);

    public static void toggleTheme() {
        darkMode.set(!darkMode.get());
    }

    public static boolean isDarkMode() {
        return darkMode.get();
    }

    public static void applyTheme(Scene scene) {
        // Apply our stunning GitHub CSS file automatically
        scene.getStylesheets().clear();
        String cssPath = ThemeManager.class.getResource("/css/github-theme.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        updateThemeClass(scene);

        // Bind an automatic listener! If the mode switches, every active scene will perfectly swap instantly!
        darkMode.addListener((obs, oldVal, newVal) -> updateThemeClass(scene));
    }

    private static void updateThemeClass(Scene scene) {
        if (scene.getRoot() != null) {
            scene.getRoot().getStyleClass().remove("dark-mode");
            scene.getRoot().getStyleClass().remove("light-mode");
            
            if (darkMode.get()) {
                scene.getRoot().getStyleClass().add("dark-mode");
            } else {
                scene.getRoot().getStyleClass().add("light-mode");
            }
        }
    }
}
