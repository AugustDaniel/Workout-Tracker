package client.browse;

import data.Workout;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BrowseTabController implements Initializable {

    @FXML
    private TableView<Workout> browse_workouts_table;
    @FXML
    public TableColumn<Workout, String> browse_workouts_table_name_column;
    @FXML
    private Button browse_uploadworkout_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateBrowseTab();
    }

    @FXML
    public void handleConnectButton(ActionEvent actionEvent) {
        try {
            ServerHandler.instance.connect();
            updateBrowseTab();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void updateBrowseTab() {
        List<Workout> workouts = ServerHandler.instance.getServerWorkouts();

        if (workouts == null) {
            return;
        }

        browse_workouts_table.getItems().setAll(workouts);
        browse_workouts_table_name_column.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().toString()));
    }

    @FXML
    public void handleRefreshButton() {
        updateBrowseTab();
    }

    @FXML
    private void handlesUploadButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/workoutUploader.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage currentStage = (Stage) browse_uploadworkout_button.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
