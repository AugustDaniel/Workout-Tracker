package client.browse;

import client.Client;
import data.Workout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
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
        if (workoutUploader_workouts_list.getSelectionModel().getSelectedItems() == null) {
            return;
        }

        try {
            ServerHandler.instance.uploadWorkout(workoutUploader_workouts_list.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            System.out.println(e);
        }

        handleBackButton(null);
    }
}
