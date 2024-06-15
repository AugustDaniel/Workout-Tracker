package client.statistics;

import data.Exercise;
import data.ExerciseSet;


public class AverageCalculator {
    static double[] kilos;
    static int amount;


    public static double getAverage(Exercise exercise) {
        amount = 0;

        for (ExerciseSet exerciseSet : exercise.getSets()) {
            amount += 1;
            System.out.println(exerciseSet.getKilos());
        }

        kilos = new double[amount];
        amount = 0;


        for (ExerciseSet exerciseSet : exercise.getSets()) {
            kilos[amount] = exerciseSet.getKilos();
            amount+=1;
        }

        System.out.println(kilos.length);



        return SumCalculator.sum(kilos)/(amount);
    }
}
