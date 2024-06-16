package client;

import data.Exercise;
import data.Workout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.IOHelper;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Client extends Application {
    private static Set<Workout> workouts = new LinkedHashSet<>();
    private static final Set<Exercise> exercises = new LinkedHashSet<>();
    private static List<String> workoutHistory = new ArrayList<>();
    private static Workout actualWorkout;
    private static final File workoutsFile = new File("workouts_client");
    private static final File workoutsHistoryFile = new File("workouts_history_client");

    @Override
    public void start(Stage primaryStage) {
        setData();
        primaryStage.setTitle("Workout Tracker");
        goToScene("/fxml/main.fxml", primaryStage);
        Runtime.getRuntime().addShutdownHook(new Thread(Client::saveWorkouts));
    }

    public void setData() {
        try {
            workouts = (Set<Workout>) IOHelper.readObject(workoutsFile);
            workoutHistory = (List<String>) IOHelper.readObject(workoutsHistoryFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        workouts.forEach(w -> exercises.addAll(w.getExercises()));
    }

    public static void addWorkout(Workout workout) {
        workouts.add(workout);
        exercises.addAll(workout.getExercises());
        saveWorkouts();
    }

    public static void finishWorkouts() {
        workoutHistory.add(LocalDate.now() + ": " + actualWorkout.getName());
        saveWorkouts();
    }

    public static void saveWorkouts() {
        try {
            IOHelper.saveObject(workouts, workoutsFile);
            IOHelper.saveObject(workoutHistory, workoutsHistoryFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void goToScene(String fxmlPath, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource(fxmlPath));
            Parent root1 = fxmlLoader.load();
            Scene scene = new Scene(root1);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<Workout> getWorkouts() {
        return workouts;
    }

    public static Set<Exercise> getExercises() {
        return exercises;
    }

    public static List<String> getWorkoutHistory() {
        return workoutHistory;
    }

    public static Workout getActualWorkout() {
        return actualWorkout;
    }

    public static void setActualWorkout(Workout actualWorkout) {
        Client.actualWorkout = actualWorkout;
    }
}
