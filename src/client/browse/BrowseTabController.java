package client.browse;

import client.Client;
import data.Workout;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BrowseTabController implements Initializable {
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

    private Map<String, List<Workout>> workouts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServerHandler.instance.connect();
        refreshWorkouts();
        updateBrowseTab();
    }

    @FXML
    public void updateBrowseTab() {
        if (workouts == null) {
            return;
        }

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
    public void handleRefreshButton() {
        refreshWorkouts();
        updateBrowseTab();
    }

    private void refreshWorkouts() {
        workouts = ServerHandler.instance.getServerWorkouts();
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
        refreshWorkouts();

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
