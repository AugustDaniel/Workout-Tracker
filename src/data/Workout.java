package data;

import java.util.ArrayList;
import java.util.List;

public class Workout {

    private List<Exercise> excercises;

    public Workout() {
        this.excercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise) {
        excercises.add(exercise);
    }

    public List<Exercise> getExcercises() {
        return excercises;
    }
}
