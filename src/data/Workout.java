package data;

import java.util.ArrayList;

public class Workout {

    private ArrayList<Exercise> excercises;

    public Workout (){
        this.excercises=new ArrayList<>();
    }

    public void addExercise(Exercise exercise){
        excercises.add(exercise);
    }
}
