package data;

import java.io.Serializable;

public class ExerciseSet implements Serializable {
    private int reps;
    private double kilos;

    public ExerciseSet(int reps, double kilos) {
        this.reps = reps;
        this.kilos = kilos;
    }

    public int getReps() {
        return reps;
    }

    public double getKilos() {
        return kilos;
    }

    @Override
    public String toString() {
        return " reps: " + reps + ",  kilos: " + kilos;
    }
}
