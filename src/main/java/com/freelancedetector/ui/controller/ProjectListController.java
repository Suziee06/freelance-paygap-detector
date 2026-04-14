package com.freelancedetector.ui.controller;

import com.freelancedetector.ui.utils.ApiClient;
import com.freelancedetector.ui.utils.SceneManager;
import com.freelancedetector.ui.utils.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectListController {

    @FXML private TableView<Map<String, Object>> projectTable;
    @FXML private TableColumn<Map<String, Object>, String> titleCol;
    @FXML private TableColumn<Map<String, Object>, String> typeCol;
    @FXML private TableColumn<Map<String, Object>, Double> amountCol;
    @FXML private TableColumn<Map<String, Object>, String> statusCol;
    @FXML private TableColumn<Map<String, Object>, String> riskCol;
    @FXML private ProgressIndicator loadingSpinner;

    @FXML
    public void initialize() {
        setupTable();
        loadProjects();
    }

    private void setupTable() {
        // We use maps here for pure dynamic JSON binding flexibility on the UI
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("projectType"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("agreedAmount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        // Risk logic implementation involves joining /risk endpoint or DTO object mapping
        
        projectTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && projectTable.getSelectionModel().getSelectedItem() != null) {
                // In future: pass the ID to ProjectDetailScreen before switching
                SceneManager.switchScene("/fxml/ProjectDetailScreen.fxml", "Project Details");
            }
        });
    }

    private void loadProjects() {
        loadingSpinner.setVisible(true);
        Long userId = SessionManager.getInstance().getLoggedInUserId();
        
        ApiClient.get("/projects/user/" + userId).thenAccept(response -> {
            Platform.runLater(() -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, Object>> list = mapper.readValue(response, new TypeReference<List<Map<String,Object>>>(){});
                    ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(list);
                    projectTable.setItems(observableList);
                } catch (Exception e) {
                    System.err.println("Parse Error: " + e.getMessage());
                } finally {
                    loadingSpinner.setVisible(false);
                }
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> loadingSpinner.setVisible(false));
            return null;
        });
    }

    @FXML
    public void goBack() {
        SceneManager.switchScene("/fxml/DashboardScreen.fxml", "Dashboard");
    }
}
