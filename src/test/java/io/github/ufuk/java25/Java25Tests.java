package io.github.ufuk.java25;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/// Further readings:
/// - [JDK 25 Release Notes](https://www.oracle.com/java/technologies/javase/25all-relnotes.html)
/// - [Java 25 Features (with Examples)](https://www.happycoders.eu/java/java-25-features/)
/// - [Java 25 Features](https://www.baeldung.com/java-25-features)
class Java25Tests {

    @Test
    void structured_concurrency_when_all_tasks_successfully_completed() throws Exception { // preview in Java 21, changed in Java 25, released in Java ?
        // Java 25 equivalent of Java21Tests#structured_concurrency_when_all_tasks_successfully_completed
        // Joiner.awaitAll() replaces `new StructuredTaskScope<>()`: waits for all subtasks to finish
        // regardless of success or failure, then results are retrieved via subtask.get().
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())) {
            var task1 = scope.fork(() -> {
                System.out.println("Task 1 started: " + Thread.currentThread()); // As you may have noticed from the outputs, these threads are "lightweight" virtual threads in a ForkJoinPool
                Thread.sleep(Duration.ofMillis(100));
                System.out.println("Task 1 finished: " + Thread.currentThread());
                return "Result 1";
            });

            var task2 = scope.fork(() -> {
                System.out.println("Task 2 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofMillis(200));
                System.out.println("Task 2 finished: " + Thread.currentThread());
                return "Result 2";
            });

            // Waits for all tasks to complete
            scope.join();

            // Gets results
            System.out.println("Result of Task 1: " + task1.get());
            System.out.println("Result of Task 2: " + task2.get());
            assertThat(task1.get()).isEqualTo("Result 1");
            assertThat(task2.get()).isEqualTo("Result 2");
        }
    }

    @Test
    void structured_concurrency_when_some_tasks_failed() throws Exception { // preview in Java 21, changed in Java 25, released in Java ?
        // Java 25 equivalent of Java21Tests#structured_concurrency_when_some_tasks_failed
        // Joiner.awaitAll() still waits for all tasks even if some fail.
        // Calling get() on a failed subtask throws IllegalStateException (same as Java 21 behavior).
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())) {
            var task1 = scope.fork(() -> {
                System.out.println("Task 1 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofMillis(100));
                throw new RuntimeException("Task 1 failed!");
            });

            var task2 = scope.fork(() -> {
                System.out.println("Task 2 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofMillis(200));
                System.out.println("Task 2 finished: " + Thread.currentThread());
                return "Result 2";
            });

            // Waits for all tasks to complete
            scope.join();

            System.out.println("Result of Task 2: " + task2.get()); // This will get result even if Task 1 failed
            assertThat(task2.get()).isEqualTo("Result 2");

            assertThatThrownBy(task1::get) // This will throw IllegalStateException, because Task 1 failed
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    /*
    Joiner.anySuccessfulResultOrThrow() was introduced in Java 25 (JEP 505) as the replacement for ShutdownOnSuccess.
    It was renamed to anySuccessfulOrThrow() in Java 26 (JEP 525) as part of the Joiner API simplification.
    The original Java 25 method no longer exists in modern JDKs, so the test below is commented out.
    For the updated example using anySuccessfulOrThrow(), see Java26Tests#structured_concurrency_when_first_successful_task_shuts_down_others.

    @Test
    void structured_concurrency_when_first_successful_task_shuts_down_others() throws Exception { // preview in Java 21, changed in Java 25, dropped in Java 26
        // Joiner.anySuccessfulResultOrThrow() replaces `ShutdownOnSuccess`: shuts down the scope
        // as soon as any subtask completes successfully and returns its result via scope.join().
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.<String>anySuccessfulResultOrThrow())) {
            var task1 = scope.fork(() -> {
                Thread.sleep(Duration.ofMillis(100)); // Finishes first
                return "Result 1";
            });
            var task2 = scope.fork(() -> {
                Thread.sleep(Duration.ofMillis(200)); // Cancelled because Task 1 finishes first
                return "Result 2";
            });
            String firstResult = scope.join(); // Returns the first successful result directly
            System.out.println("First completed result: " + firstResult);
        }
    }
    */

    @Test
    void structured_concurrency_when_any_task_fails_shuts_down_others() throws Exception { // preview in Java 21, changed in Java 25, released in Java ?
        // Java 25 equivalent of Java21Tests#structured_concurrency_when_some_tasks_failed_but_shutdown_on_failure
        // Joiner.awaitAllSuccessfulOrThrow() replaces `ShutdownOnFailure`: shuts down the scope
        // as soon as any subtask fails, then join() throws propagating the failure.
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())) {
            var task0 = scope.fork(() -> {
                System.out.println("Task 0 started: " + Thread.currentThread());
                System.out.println("Task 0 finished: " + Thread.currentThread());
                return "Result 0";
            });

            var task1 = scope.fork(() -> {
                System.out.println("Task 1 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofMillis(200));
                System.out.println("Task 1 finished: " + Thread.currentThread());
                return "Result 1";
            });

            var task2 = scope.fork(() -> {
                System.out.println("Task 2 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofMillis(100));
                throw new RuntimeException("Task 2 failed!"); // Triggers shutdown of Task 1
            });

            assertThatThrownBy(scope::join) // join() throws because Task 2 failed before Task 1 completed
                    .isInstanceOf(Exception.class);
        }
    }

}
