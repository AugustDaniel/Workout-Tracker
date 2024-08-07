package client.statistics;

import data.Exercise;

import java.util.concurrent.atomic.AtomicInteger;

public class AverageCalculator {
    private static double[] kilos;
    private static double[] reps;

    private static void setup(Exercise exercise) {
        kilos = new double[exercise.getSets().size()];
        reps = new double[exercise.getSets().size()];

        AtomicInteger counter = new AtomicInteger();
        exercise.getSets().forEach(exerciseSet -> {
            kilos[counter.get()] = exerciseSet.getKilos();
            reps[counter.get()] = exerciseSet.getReps();
            counter.getAndIncrement();
        });
    }

    public static double getAverageReps(Exercise exercise) {
        setup(exercise);
        return SumCalculator.sum(reps) / exercise.getSets().size();
    }

    public static double getAverageKilos(Exercise exercise) {
        setup(exercise);
        return SumCalculator.sum(kilos) / exercise.getSets().size();
    }
}
