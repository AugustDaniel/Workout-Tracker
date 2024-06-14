package client;

import data.Exercise;
import data.ExerciseSet;
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
        if(Client.getActualWorkout()==null){
            Client.setActualWorkout(new Workout("new workout"));
        }
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
        Client.setActualWorkout(workouts_workouts_list.getSelectionModel().getSelectedItem());
        if(Client.getActualWorkout()!=null) {
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

    }

    @FXML
    private void handlesStartButton() {
        Client.setActualWorkout(workouts_workouts_list.getSelectionModel().getSelectedItem());
        if(Client.getActualWorkout()!=null) {
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
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Workout workout : Client.getWorkouts()) {
            double totalKilos = 0;
            double totalReps = 0;
            int totalSets = 0;

            for (Exercise exercise : workout.getExcercises()) {
                for (ExerciseSet exerciseSet : exercise.getSets()) {
                    totalKilos += exerciseSet.getKilos();
                    totalReps += exerciseSet.getReps();
                    totalSets += 1;
                }
            }

            if(totalSets>0) {
                double averageKilos = totalKilos / totalSets;
                double averageReps = totalReps / totalSets;

                System.out.println("Workout: " + workout.getName());
                System.out.println("Average Kilos: " + averageKilos);
                System.out.println("Average Reps: " + averageReps);


                series.getData().add(new XYChart.Data<>(workout.getName(), averageKilos));

            }
        }


        statistics_workoutduration_graph.getData().add(series);
        statistics_workoutduration_graph.setLegendVisible(false);
        setData();


    }

    public void setData(){
        for (Workout workout:Client.getWorkouts()) {
            workouts_workouts_list.getItems().add(workout);
        }

    }


    public void handleConnectButton(ActionEvent actionEvent) {
        try {
            ServerHandler.instance.connect();
            browse_workouts_table.setItems(ServerHandler.instance.getServerWorkouts());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
