package client.statistics;

import data.Exercise;
import data.ExerciseSet;


public class AverageCalculator {
    static double[] kilos;
    static double[] reps;
    static int amountKilos;
    static int amountReps;


    public static double getAverage(Exercise exercise) {
        amountKilos = 0;
        amountReps = 0;

        for (ExerciseSet exerciseSet : exercise.getSets()) {
            amountKilos += 1;
            amountReps += 1;
        }

        kilos = new double[amountKilos];
        amountKilos = 0;

        reps = new double[amountReps];
        amountReps = 0;

        for (ExerciseSet exerciseSet : exercise.getSets()) {
            kilos[amountKilos] = exerciseSet.getKilos();
            amountKilos+=1;

            reps[amountReps]=exerciseSet.getReps();
            amountReps+=1;
        }

        return SumCalculator.sum(kilos)/(kilos.length);
    }

    public static double getAverageReps(Exercise exercise){
        getAverage(exercise);
        return SumCalculator.sum(reps)/amountReps;
    }

    public static double getAverageKilos(Exercise exercise){
        getAverage(exercise);
        return SumCalculator.sum(kilos)/amountKilos;
    }

}
