package client.statistics;

import client.Client;
import data.Exercise;
import data.ExerciseSet;
import data.Workout;
import javafx.collections.ObservableList;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("workout 1", 23));
        series.getData().add(new XYChart.Data<>("workout 2", 14));
        series.getData().add(new XYChart.Data<>("workout 3", 15));
        series.getData().add(new XYChart.Data<>("workout 4", 24));
        series.getData().add(new XYChart.Data<>("workout 5", 34));

        statistics_workoutduration_graph.getData().add(series);
        statistics_workoutduration_graph.setLegendVisible(false);


        for (Workout workout:Client.getWorkouts()) {
            for (Exercise exercise: workout.getExcercises()) {
                statistics_exercises_list.getItems().add(exercise);
            }
        }
        System.out.println(statistics_exercises_list.getItems());
    }

}
