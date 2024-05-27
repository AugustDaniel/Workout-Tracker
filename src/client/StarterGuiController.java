package client;

import data.Exercise;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StarterGuiController {
    @FXML
    private Button workoutstarter_back_button;
    @FXML
    private ListView<Exercise> workoutstarter_exercises_list;
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
//            workoutstarter_exercises_list.getSelectionModel().getSelectedItems().get(0).addSet();
        }
    }

    public void handleFinishButton(ActionEvent actionEvent) {
        //todo
    }

    public void initialize() {
        workoutstarter_exercises_list.getItems().setAll(Client.getActualWorkout().getExcercises());
    }
}
