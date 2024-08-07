package client.browse;

import client.Client;
import client.SubMenu;
import data.Exercise;
import data.Workout;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BrowseTabController {
    @FXML
    public TextField browse_search_text_field;
    @FXML
    public VBox browse_tab_pane;
    @FXML
    private TableView<WorkoutTableRow> browse_workouts_table;
    @FXML
    public TableColumn<WorkoutTableRow, String> browse_workouts_table_name_column;
    @FXML
    public TableColumn<WorkoutTableRow, String> browse_workouts_table_uploader_column;
    private Map<String, List<Workout>> workouts = new HashMap<>();
    private final ExecutorService thread = Executors.newSingleThreadExecutor();

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
        workouts.forEach((uploader, value) -> {
            value.forEach(w -> workoutTableRows.add(new WorkoutTableRow(w, uploader)));
        });

        browse_workouts_table.getItems().setAll(workoutTableRows);
        browse_workouts_table_name_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        browse_workouts_table_uploader_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUploader()));
    }

    @FXML
    private void handlesUploadButton() {
        goToScreen("/fxml/workoutUploader.fxml");
    }

    @FXML
    private void handleSaveButton() {
        WorkoutTableRow row = browse_workouts_table.getSelectionModel().getSelectedItem();

        if (row == null) {
            return;
        }

        Workout clickedWorkout = row.getWorkout();
        Workout toBeAdded = new Workout(clickedWorkout.getName());
        Set<Exercise> savedExercises = Client.getExercises();

        clickedWorkout.getExercises().forEach(exercise -> {
            if (savedExercises.stream().anyMatch(e -> e.equals(exercise))) {
                savedExercises.stream()
                        .filter(e -> e.equals(exercise))
                        .findFirst()
                        .ifPresent(toBeAdded::addExercises);
            } else {
                toBeAdded.addExercises(exercise);
            }
        });

        Client.addWorkout(toBeAdded);
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

    private void goToScreen(String path) {
        try {
            thread.shutdownNow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent newContent = loader.load();
            SubMenu menu = loader.getController();
            menu.setMenu(browse_tab_pane);
            browse_tab_pane.getChildren().setAll(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
