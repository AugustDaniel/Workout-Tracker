package client.statistics;

import data.Exercise;
import data.ExerciseSet;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public class VarianceCalculator {

    private static double average;
    private static final AtomicInteger amount = new AtomicInteger(0);

    public static synchronized double getVariance(Exercise exercise) {
        ForkJoinPool pool = new ForkJoinPool();
        amount.set(0);
        average = AverageCalculator.getAverage(exercise);
        return pool.invoke(new VarianceSetAdder(exercise.getSets())) / amount.get();
    }

    private static class VarianceSetAdder extends RecursiveTask<Double> {

        private final List<ExerciseSet> sets;

        public VarianceSetAdder(List<ExerciseSet> sets ) {
            this.sets = sets;
        }

        @Override
        protected Double compute() {
            if (sets.size() == 1) {
                amount.incrementAndGet();
                return Math.pow(sets.get(0).getKilos() - average, 2);
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
