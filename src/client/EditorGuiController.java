package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EditorGuiController {

    @FXML
    private Button workouteditor_back_button;

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

    public void handleAddToButton(ActionEvent actionEvent) {
    }

    public void handleRemoveFromButton(ActionEvent actionEvent) {
    }

    public void handleSaveButton(ActionEvent actionEvent) {
    }
}
