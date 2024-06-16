package client.statistics;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumCalculator {

    public static double sum(double[] list) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new DoubleAdder(list));
    }

    private static class DoubleAdder extends RecursiveTask<Double> {

        private final double[] list;

        public DoubleAdder(double[] list) {
            if (list.length == 0) {
                throw new IllegalArgumentException();
            }

            this.list = list;
        }

        @Override
        protected Double compute() {
            if (this.list.length == 1) {
                return list[0];
            }

            double[] firstHalf = Arrays.copyOfRange(this.list, 0, this.list.length / 2);
            double[] secondHalf = Arrays.copyOfRange(this.list, this.list.length / 2, this.list.length);

            DoubleAdder firstTask = new DoubleAdder(firstHalf);
            DoubleAdder secondTask = new DoubleAdder(secondHalf);

            return firstTask.invoke() + secondTask.invoke();
        }
    }
}
