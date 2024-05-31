package client.browse;

import client.Client;
import client.SubMenu;
import data.Workout;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class WorkoutUploaderController implements Initializable, SubMenu {
    @FXML
    public ListView<Workout> workoutUploader_workouts_list;
    @FXML
    public TextField workoutUploader_name_textfield;
    @FXML
    public Button workoutUploader_upload_button;
    @FXML
    private Button workoutUploader_back_button;

    private Parent root;
    private String rootPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workoutUploader_workouts_list.getItems().setAll(Client.getWorkouts());
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/browseTab.fxml")));
            ((VBox) root).getChildren().setAll(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUploadButton(ActionEvent actionEvent) {
        if (workoutUploader_workouts_list.getSelectionModel().getSelectedItems() == null || workoutUploader_name_textfield.getText().isEmpty()) {
            return; //todo add more robust error handling
        }

        new Thread(() -> {
            try {
                ServerHandler.uploadWorkout(new AbstractMap.SimpleEntry<>(workoutUploader_name_textfield.getText(), workoutUploader_workouts_list.getSelectionModel().getSelectedItem()));
                Platform.runLater(ServerHandler::showUploadSuccessful);
            } catch (IOException e) {
                Platform.runLater(ServerHandler::showServerError);
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void setMenu(Parent parent) {
        root = parent;
    }
}
