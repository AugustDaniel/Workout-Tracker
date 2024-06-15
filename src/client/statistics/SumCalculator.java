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
        private Double sum;

        public DoubleAdder(double[] list, Double sum) {
            this.list = list;
            this.sum = sum;
        }

        public DoubleAdder(double[] list) {
            this(list, 0.0);
        }

        @Override
        protected Double compute() {
            if (this.list.length == 1) {
                this.sum += this.list[0];
                return this.list[0];
            }

            double[] firstHalf = Arrays.copyOfRange(this.list, 0, this.list.length / 2);
            double[] secondHalf = Arrays.copyOfRange(this.list, this.list.length / 2, this.list.length);

            invokeAll(new DoubleAdder(firstHalf, this.sum), new DoubleAdder(secondHalf, this.sum));

            return this.sum;
        }
    }
}
