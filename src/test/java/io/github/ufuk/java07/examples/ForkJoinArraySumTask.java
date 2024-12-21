package io.github.ufuk.java07.examples;

import java.util.concurrent.RecursiveTask;

public class ForkJoinArraySumTask extends RecursiveTask<Integer> {

    private final int threshold;

    private final int[] array;

    private final int startIndex;

    private final int endIndex;

    public ForkJoinArraySumTask(int threshold, int[] array, int startIndex, int endIndex) {
        this.threshold = threshold;
        this.array = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected Integer compute() {
        System.out.println("Thread: " + Thread.currentThread());
        if (endIndex - startIndex <= threshold) { // If task is fairly small, then compute
            int sum = 0;
            for (int i = startIndex; i < endIndex; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            // Divide task (fork)
            int mid = (startIndex + endIndex) / 2;
            ForkJoinArraySumTask leftTask = new ForkJoinArraySumTask(threshold, array, startIndex, mid);
            ForkJoinArraySumTask rightTask = new ForkJoinArraySumTask(threshold, array, mid, endIndex);

            // Run tasks in parallel
            leftTask.fork();
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();

            // Sum results (join)
            return leftResult + rightResult;
        }
    }

}
