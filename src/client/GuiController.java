package client;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import data.Workout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class GuiController implements Initializable {

    @FXML
    private Button workouts_create_button;
    @FXML
    private Button workouts_edit_button;
    @FXML
    private Button workouts_start_button;
    @FXML
    private Button browse_uploadworkout_button;
    @FXML
    private ListView<Workout> workouts_workouts_list;
    @FXML
    private LineChart<String, Number> statistics_workoutduration_graph;
    @FXML
    private TableView<Workout> browse_workouts_table;

    @FXML
    private void handleCreateButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/workoutEditor.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage currentStage = (Stage) workouts_create_button.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/workoutEditor.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage currentStage = (Stage) workouts_edit_button.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handlesStartButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/workoutStarter.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage currentStage = (Stage) workouts_start_button.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workouts_workouts_list.getItems().add(new Workout("Test Workout"));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("workout 1", 23));
        series.getData().add(new XYChart.Data<>("workout 2", 14));
        series.getData().add(new XYChart.Data<>("workout 3", 15));
        series.getData().add(new XYChart.Data<>("workout 4", 24));
        series.getData().add(new XYChart.Data<>("workout 5", 34));

        statistics_workoutduration_graph.getData().add(series);
        statistics_workoutduration_graph.setLegendVisible(false);


    }

    public void handleConnectButton(ActionEvent actionEvent) {
        try {
            ServerHandler.instance.connect();
            List<Workout> workouts = ServerHandler.instance.getServerWorkouts();
            browse_workouts_table.getItems().setAll(workouts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
