package client;

import data.Exercise;
import data.Workout;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditorGuiController {

    @FXML
    private Button workouteditor_back_button;
    @FXML
    private ListView<Exercise> workouteditor_exercises_list;
    @FXML
    private ListView<Exercise> workouteditor_exercisesinworkout_list;
    @FXML
    private TextField workouteditor_workoutname_textfield;
    @FXML
    private TextField workouteditor_addexercise_textfield;

    public void handleAddButton() {
        String inputText = workouteditor_addexercise_textfield.getText();
        String regex = "^[a-zA-Z0-9\\s]+$"; //no special characters
        if (inputText.matches(regex)) {
            Client.getExercises().add(new Exercise(inputText));
            workouteditor_exercises_list.getItems().clear();
            workouteditor_exercises_list.getItems().addAll(Client.getExercises());
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid exercise name", ButtonType.CLOSE);
        }
    }

    public void handleBackButton() {
        Client.goToScene("/fxml/main.fxml", (Stage) workouteditor_back_button.getScene().getWindow());
    }


    public void initialize() {
        Client.getExercises().forEach(e -> workouteditor_exercises_list.getItems().add(e));

        workouteditor_exercisesinworkout_list.getItems().addAll(Client.getActualWorkout().getExercises());
        workouteditor_workoutname_textfield.setText(Client.getActualWorkout().getName());
    }

    public void handleAddToButton() {
        if (workouteditor_exercises_list.getSelectionModel().getSelectedItem() != null) {
            workouteditor_exercisesinworkout_list.getItems().add(workouteditor_exercises_list.getSelectionModel().getSelectedItem());
        }
    }

    public void handleRemoveFromButton() {
        if (workouteditor_exercisesinworkout_list.getSelectionModel().getSelectedItem() != null) {
            workouteditor_exercisesinworkout_list.getItems().remove(workouteditor_exercisesinworkout_list.getSelectionModel().getSelectedItem());
        }
    }

    public void handleSaveButton() {
        Workout actualWorkout = Client.getActualWorkout();
        actualWorkout.setName(workouteditor_workoutname_textfield.getText());

        ArrayList<Exercise> exercises = new ArrayList<>(workouteditor_exercisesinworkout_list.getItems());
        actualWorkout.setExercises(exercises);

        Client.addWorkout(actualWorkout);
        Client.setActualWorkout(null);
        handleBackButton();
    }
}
