package client.browse;

import client.Client;
import client.SubMenu;
import data.Workout;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkoutUploaderController implements Initializable, SubMenu {
    @FXML
    public ListView<Workout> workoutUploader_workouts_list;
    @FXML
    public TextField workoutUploader_name_textfield;
    @FXML
    public Button workoutUploader_upload_button;
    @FXML
    public Button workoutUploader_back_button;
    private Parent root;
    private final ExecutorService thread = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workoutUploader_workouts_list.getItems().setAll(Client.getWorkouts());
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/browseTab.fxml")));
            thread.shutdownNow();
            ((VBox) root).getChildren().setAll(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUploadButton(ActionEvent actionEvent) {
        if (workoutUploader_workouts_list.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "Please select workout", ButtonType.CLOSE).showAndWait();
            return;
        }

        if (workoutUploader_name_textfield.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter uploader name", ButtonType.CLOSE).showAndWait();
            return;
        }

        thread.execute(() -> {
            try {
                Workout workout = workoutUploader_workouts_list.getSelectionModel().getSelectedItem();
                String text = workoutUploader_name_textfield.getText();
                ServerHandler.uploadWorkout(new AbstractMap.SimpleEntry<>(text, workout));
                Platform.runLater(ServerHandler::showUploadSuccessful);
            } catch (IOException e) {
                Platform.runLater(ServerHandler::showServerError);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setMenu(Parent parent) {
        root = parent;
    }
}
