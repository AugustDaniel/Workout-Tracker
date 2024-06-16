package client.statistics;

import client.Client;
import data.Exercise;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

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
        statistics_exercises_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            updateStatistics(newValue);
        });


        statistics_workoutduration_graph.getData().add(series);
        statistics_workoutduration_graph.setLegendVisible(false);
        statistics_exercises_list.getItems().addAll(Client.getExercises());
    }

    public void updateStatistics(Exercise exercise) {
        if (exercise.getSets().isEmpty()) {
            statistics_variance_reps_text.setText("");
            statistics_variance_kilos_text.setText("");
            statistics_average_kilos_text.setText("");
            statistics_average_reps_text.setText("");
            series.getData().clear();
            return;
        }

        DecimalFormat df = new DecimalFormat("###.###");
        statistics_variance_reps_text.setText(String.valueOf(df.format(VarianceCalculator.getVarianceReps(exercise))));
        statistics_variance_kilos_text.setText(String.valueOf(df.format(VarianceCalculator.getVarianceKilos(exercise))));
        statistics_average_kilos_text.setText(String.valueOf(df.format(AverageCalculator.getAverageKilos(exercise))));
        statistics_average_reps_text.setText(String.valueOf(df.format(AverageCalculator.getAverageReps(exercise))));
        series.getData().clear();

        AtomicInteger i = new AtomicInteger();
        exercise.getSets().forEach(set -> {
            if (!exercise.getSets().isEmpty()) {
                series.getData().add(new XYChart.Data<>("set" + i, set.getKilos()));
                i.addAndGet(1);
            }
        });
    }

}
