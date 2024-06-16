package client;

import data.Workout;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class GuiController implements Initializable {
    @FXML
    public ListView<String> workouts_lastworkout_list;
    @FXML
    private Button workouts_create_button;
    @FXML
    private Button workouts_edit_button;
    @FXML
    private Button workouts_start_button;
    @FXML
    private ListView<Workout> workouts_workouts_list;

    @FXML
    private void handleCreateButton() {
        Client.setActualWorkout(new Workout("new workout"));
        Client.goToScene("/fxml/workoutEditor.fxml", (Stage) workouts_create_button.getScene().getWindow());
    }

    @FXML
    private void handleEditButton() {
        Workout workout = workouts_workouts_list.getSelectionModel().getSelectedItem();
        if (workout == null) {
            return;
        }

        Client.setActualWorkout(workout);
        Client.goToScene("/fxml/workoutEditor.fxml", (Stage) workouts_edit_button.getScene().getWindow());
    }

    @FXML
    private void handlesStartButton() {
        Workout workout = workouts_workouts_list.getSelectionModel().getSelectedItem();
        if (workout == null) {
            return;
        }

        Client.setActualWorkout(workout);
        Client.goToScene("/fxml/workoutStarter.fxml", (Stage) workouts_start_button.getScene().getWindow());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    public void setData() {
        workouts_workouts_list.getItems().addAll(Client.getWorkouts());
        workouts_lastworkout_list.getItems().addAll(Client.getWorkoutHistory());
    }
}
