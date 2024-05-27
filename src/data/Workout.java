package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Workout implements Serializable {

    private String name;
    private List<Exercise> excercises;

    public Workout(String name) {
        this.name = name;
        this.excercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise) {
        excercises.add(exercise);
    }

    public List<Exercise> getExcercises() {
        return excercises;
    }

    @Override
    public String toString() {
        return name +" "+ excercises.size() +" exercises";
    }
}
