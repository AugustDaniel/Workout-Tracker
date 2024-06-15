package client.statistics;

import data.Exercise;
import data.ExerciseSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class VarianceCalculator {

    private static double average;

    public static double getVariance(Exercise exercise) {
        ForkJoinPool pool = new ForkJoinPool();
        //TODO uncomment when method is done
//        average = AverageCalculator.getAverage(exercise);
        return pool.invoke(new VarianceSetAdder(exercise.getSets()));
    }

    private static class VarianceSetAdder extends RecursiveTask<Double> {

        private final List<ExerciseSet> sets;

        public VarianceSetAdder(List<ExerciseSet> sets) {
            this.sets = sets;
        }

        @Override
        protected Double compute() {
            if (sets.size() == 1) {
                return Math.pow(sets.get(0).getKilos() - average, 2);
            }

            return null;
        }
    }
}
