package client.statistics;

import data.Exercise;
import data.ExerciseSet;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public class VarianceCalculator {

    public enum Option {KILOS, REPS}

    private static double average;
    private static final AtomicInteger amount = new AtomicInteger(0);
    private static Option currentOption;

    public static double getVarianceKilos(Exercise exercise) {
        return getVariance(exercise, Option.KILOS);
    }

    public static double getVarianceReps(Exercise exercise) {
        return getVariance(exercise, Option.REPS);
    }

    private static synchronized double getVariance(Exercise exercise, Option option) {
        ForkJoinPool pool = new ForkJoinPool();
        amount.set(0);
        currentOption = option;

        switch (currentOption) {
            case KILOS: average = AverageCalculator.getAverageKilos(exercise); break;
            case REPS: average = AverageCalculator.getAverageReps(exercise); break;
        }

        return pool.invoke(new VarianceSetAdder(exercise.getSets())) / amount.get();
    }

    private static class VarianceSetAdder extends RecursiveTask<Double> {

        private final List<ExerciseSet> sets;

        public VarianceSetAdder(List<ExerciseSet> sets) {
            this.sets = sets;
        }

        @Override
        protected Double compute() {
            if (sets.size() == 1) {
                amount.incrementAndGet();

                double value;
                switch (currentOption) {
                    case KILOS:
                        value = sets.get(0).getKilos();
                        break;
                    case REPS:
                        value = sets.get(0).getReps();
                        break;
                    default:
                        value = 0;
                }

                return Math.pow(value - average, 2);
            }

            int middleIndex = sets.size() / 2;
            List<ExerciseSet> firstHalf = sets.subList(0, middleIndex);
            List<ExerciseSet> secondHalf = sets.subList(middleIndex, sets.size());

            VarianceSetAdder firstTask = new VarianceSetAdder(firstHalf);
            VarianceSetAdder secondTask = new VarianceSetAdder(secondHalf);

            return firstTask.invoke() + secondTask.invoke();
        }
    }
}
