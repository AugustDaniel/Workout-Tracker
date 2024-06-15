package client.statistics;

import client.Client;
import data.Exercise;
import data.ExerciseSet;
import data.Workout;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsTabController implements Initializable {

    @FXML
    public ListView<Exercise> statistics_exercises_list;
    @FXML
    public LineChart<String, Number> statistics_workoutduration_graph;
    @FXML
    public Text statistics_average_kilos_text;
    @FXML
    public Text statistics_variance_kilos_text;
    @FXML
    public Text statistics_average_reps_text;
    @FXML
    public Text statistics_variance_reps_text;

    public XYChart.Series<String, Number> series = new XYChart.Series<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        statistics_exercises_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Exercise>() {
            @Override
            public void changed(ObservableValue<? extends Exercise> observable, Exercise oldValue, Exercise newValue) {
                if (newValue != null) {
                    updateStatistics(newValue);
                }
            }
        });




        statistics_workoutduration_graph.getData().add(series);
        statistics_workoutduration_graph.setLegendVisible(false);

        for (Workout workout:Client.getWorkouts()) {
            for (Exercise exercise: workout.getExcercises()) {

                statistics_exercises_list.getItems().add(exercise);

            }
        }
    }

    public void updateStatistics(Exercise exercise){
        statistics_average_kilos_text.setText(String.valueOf(exercise.getSets().get(0).getKilos()));
        series.getData().clear();
        int i = 0;
        for (ExerciseSet set: exercise.getSets()) {
            if(!exercise.getSets().isEmpty()){
            series.getData().add(new XYChart.Data<>("set"+i, set.getKilos()));
            i+=1;}
            else {
                series.getData().clear();
            }
        }

    }

}
