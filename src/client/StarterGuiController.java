package client;

import data.Exercise;
import data.ExerciseSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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

    public void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage currentStage = (Stage) workoutstarter_back_button.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleAddButton(ActionEvent actionEvent) {
        if (workoutstarter_exercises_list.getSelectionModel().getSelectedItems()!=null){
            workoutstarter_exercises_list.getSelectionModel().getSelectedItems().get(0).addSet(new ExerciseSet(Integer.parseInt(workoutstarter_reps_textfield.getText()), Double.parseDouble(workoutstarter_kilos_textfield.getText())));
            workoutstarter_set_list.getItems().setAll(workoutstarter_exercises_list.getSelectionModel().getSelectedItem().getSets());
        }

    }

    public void handleFinishButton(ActionEvent actionEvent) {
    }

    public void initialize() {
        workoutstarter_exercises_list.getItems().setAll(Client.getActualWorkout().getExercises());
        workoutstarter_exercises_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Exercise>() {
            @Override
            public void changed(ObservableValue<? extends Exercise> observable, Exercise oldValue, Exercise newValue) {
                if (newValue != null) {
                    updateSetListView(newValue);
                }
            }
        });
    }

    private void updateSetListView(Exercise exercise) {
        workoutstarter_set_list.getItems().setAll(exercise.getSets());
    }
}
