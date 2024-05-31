package client.browse;

import client.Client;
import data.Workout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.ResourceBundle;

public class WorkoutUploaderController implements Initializable {
    @FXML
    public ListView<Workout> workoutUploader_workouts_list;
    @FXML
    public TextField workoutUploader_name_textfield;
    @FXML
    public Button workoutUploader_upload_button;
    @FXML
    private Button workoutUploader_back_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workoutUploader_workouts_list.getItems().setAll(Client.getWorkouts());
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage currentStage = (Stage) workoutUploader_back_button.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUploadButton(ActionEvent actionEvent) {
        if (workoutUploader_workouts_list.getSelectionModel().getSelectedItems() == null || workoutUploader_name_textfield.getText().isEmpty()) {
            return; //todo add more robust error handling
        }

        try {
            ServerHandler.uploadWorkout(new AbstractMap.SimpleEntry<>(workoutUploader_name_textfield.getText(), workoutUploader_workouts_list.getSelectionModel().getSelectedItem()));
        } catch (IOException e) {
            ServerHandler.showConnectionError();
        }

        handleBackButton(null);
    }
}
