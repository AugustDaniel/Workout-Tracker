package client;

import data.Exercise;
import data.Workout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Client extends Application {
    private static ArrayList<Workout> workouts;
    private static ArrayList<Exercise> exercises;
    private static Workout actualWorkout;

    @Override
    public void start(Stage primaryStage) throws Exception {

        setData();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

        }));



    }

    public void setData(){
        workouts=new ArrayList<>();
        exercises=new ArrayList<>();
        workouts.add(new Workout("workout 1"));
        workouts.add(new Workout("workout 2"));
        workouts.add(new Workout("workout 3"));
        workouts.add(new Workout("workout 4"));

        exercises.add(new Exercise("Exercise 1"));
        exercises.add(new Exercise("Exercise 2"));
        exercises.add(new Exercise("Exercise 3"));
    }

    public static ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public static ArrayList<Exercise> getExercises() {
        return exercises;
    }
    public void editWorkout(Workout workout, String name, ArrayList<Exercise> exercises){
        for (Exercise exercise: exercises) {
            workout.addExercise(exercise);
        }
        workout.setName(name);
    }

    public static void setActualWorkout(Workout actualWorkout) {
        Client.actualWorkout = actualWorkout;
    }

    public static Workout getActualWorkout() {
        return actualWorkout;
    }
}
