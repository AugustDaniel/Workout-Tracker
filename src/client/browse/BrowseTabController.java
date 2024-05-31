package client.browse;

import client.Client;
import data.Workout;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BrowseTabController {
    @FXML
    public TextField browse_search_text_field;
    @FXML
    private TableView<WorkoutTableRow> browse_workouts_table;
    @FXML
    public TableColumn<WorkoutTableRow, String> browse_workouts_table_name_column;
    @FXML
    public TableColumn<WorkoutTableRow, String> browse_workouts_table_uploader_column;
    @FXML
    private Button browse_uploadworkout_button;

    private Map<String, List<Workout>> workouts = new HashMap<>();
    private ExecutorService thread = Executors.newSingleThreadExecutor();

    @FXML
    public void handleRefreshButton() {
        refreshWorkouts();
    }

    private void refreshWorkouts() {
        thread.submit(() -> {
            try {
                workouts = ServerHandler.getServerWorkouts();
                Platform.runLater(this::updateBrowseTab);
            } catch (IOException e) {
                Platform.runLater(ServerHandler::showConnectionError);
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                Platform.runLater(ServerHandler::showServerError);
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void updateBrowseTab() {
        if (workouts == null) {
            return;
        }

        System.out.println(workouts);
        browse_workouts_table.getItems().clear();

        List<WorkoutTableRow> workoutTableRows = new ArrayList<>();
        for (Map.Entry<String, List<Workout>> entry : workouts.entrySet()) {
            String uploader = entry.getKey();
            for (Workout workout : entry.getValue()) {
                workoutTableRows.add(new WorkoutTableRow(workout, uploader));
            }
        }

        browse_workouts_table.getItems().setAll(workoutTableRows);
        browse_workouts_table_name_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        browse_workouts_table_uploader_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUploader()));
    }

    @FXML
    private void handlesUploadButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/workoutUploader.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage currentStage = (Stage) browse_uploadworkout_button.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveButton() {
        WorkoutTableRow row = browse_workouts_table.getSelectionModel().getSelectedItem();

        if (row == null) {
            return;
        }

        Client.addWorkout(row.getWorkout());
    }

    @FXML
    private void handleSearchButton() {
        String searchText = browse_search_text_field.getText().toLowerCase();

        if (workouts == null) {
            return;
        }

        if (searchText.isEmpty()) {
            refreshWorkouts();
        } else {
            workouts = workouts.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue()
                                    .stream()
                                    .filter(workout -> workout.getName().toLowerCase().contains(searchText))
                                    .collect(Collectors.toList())
                    ));
        }

        updateBrowseTab();
    }

    @FXML
    private void handleViewUploaderButton() {
        //TODO create uploader page and switch to it here
    }
}
