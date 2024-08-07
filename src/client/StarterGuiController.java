package client;

import data.Exercise;
import data.ExerciseSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StarterGuiController {
    @FXML
    private Button workoutstarter_back_button;
    @FXML
    private ListView<Exercise> workoutstarter_exercises_list;
    @FXML
    private TextField workoutstarter_reps_textfield;
    @FXML
    private TextField workoutstarter_kilos_textfield;
    @FXML
    private ListView<ExerciseSet> workoutstarter_set_list;

    public void handleBackButton() {
        Client.goToScene("/fxml/main.fxml", (Stage) workoutstarter_back_button.getScene().getWindow());
    }

    public void handleAddButton() {
        if (workoutstarter_exercises_list.getSelectionModel().getSelectedItem() == null
                || workoutstarter_reps_textfield.getText() == null
                || workoutstarter_kilos_textfield.getText() == null) {
            return;
        }

        ExerciseSet set = new ExerciseSet(Integer.parseInt(workoutstarter_reps_textfield.getText()), Double.parseDouble(workoutstarter_kilos_textfield.getText()));
        workoutstarter_exercises_list.getSelectionModel().getSelectedItem().addSet(set);
        workoutstarter_set_list.getItems().add(set);
    }

    public void handleFinishButton() {
        Client.finishWorkouts();
        handleBackButton();
    }

    public void initialize() {
        workoutstarter_exercises_list.getItems().setAll(Client.getActualWorkout().getExercises());

        workoutstarter_exercises_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            if (newValue.getSets().isEmpty()) {
                workoutstarter_set_list.getItems().clear();
            }

            workoutstarter_set_list.getItems().clear();
            workoutstarter_set_list.getItems().addAll(newValue.getSets());
        });
    }
}
