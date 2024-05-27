package client;

import data.Exercise;
import data.Workout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditorGuiController {

    @FXML
    private Button workouteditor_back_button;
    @FXML
    private ListView<Exercise>workouteditor_exercises_list;
   @FXML
    private ListView<Exercise>workouteditor_exercisesinworkout_list;
   @FXML
   private TextField workouteditor_workoutname_textfield;

    public void handleAddButton(ActionEvent actionEvent) {

    }

    public void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage currentStage = (Stage) workouteditor_back_button.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initialize() {

        for (Exercise exercise :Client.getExercises()) {
            workouteditor_exercises_list.getItems().add(exercise);
        }
        workouteditor_exercisesinworkout_list.getItems().addAll(Client.getActualWorkout().getExcercises());
        workouteditor_workoutname_textfield.setText(Client.getActualWorkout().getName());
    }

    public void handleAddToButton(ActionEvent actionEvent) {
        if(workouteditor_exercises_list.getSelectionModel().getSelectedItems()!=null) {
            workouteditor_exercisesinworkout_list.getItems().add(workouteditor_exercises_list.getSelectionModel().getSelectedItems().get(0));
        }
    }

    public void handleRemoveFromButton(ActionEvent actionEvent) {
        if(workouteditor_exercisesinworkout_list.getSelectionModel().getSelectedItems()!=null){
            workouteditor_exercisesinworkout_list.getItems().remove(workouteditor_exercisesinworkout_list.getSelectionModel().getSelectedItem());
        }
    }

    public void handleSaveButton(ActionEvent actionEvent) {
        Workout actualWorkout = Client.getActualWorkout();
        actualWorkout.setName(workouteditor_workoutname_textfield.getText());
        actualWorkout.setExcercises(workouteditor_exercisesinworkout_list.getItems());
        Client.setActualWorkout(null);
    }
}
