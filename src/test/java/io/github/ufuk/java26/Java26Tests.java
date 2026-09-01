package io.github.ufuk.java26;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/// Further readings:
/// - [JDK 26 Release Notes](https://www.oracle.com/java/technologies/javase/26all-relnotes.html)
/// - [Java 26 Features (with Examples)](https://www.happycoders.eu/java/java-26-features/)\
/// - [New Features in Java 26](https://www.baeldung.com/java-26-new-features)
class Java26Tests {

    @Test
    void structured_concurrency_when_first_successful_task_shuts_down_others() throws Exception { // preview in Java 21, changed in Java 25, changed in Java 26, released in Java ?
        // Java 26 equivalent of Java25Tests#structured_concurrency_when_first_successful_task_shuts_down_others (commented out)
        // JEP 525 (Java 26) renamed Joiner.anySuccessfulResultOrThrow() to Joiner.anySuccessfulOrThrow()
        // as part of the Joiner API simplification ("Result" dropped from method name).
        // The scope shuts down as soon as any subtask completes successfully
        // and scope.join() directly returns that subtask's result.
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.<String>anySuccessfulOrThrow())) {
            var task1 = scope.fork(() -> {
                System.out.println("Task 1 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofMillis(100)); // Finishes first
                System.out.println("Task 1 finished: " + Thread.currentThread());
                return "Result 1";
            });

            var task2 = scope.fork(() -> {
                System.out.println("Task 2 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofMillis(200)); // Cancelled because Task 1 finishes first
                System.out.println("Task 2 finished: " + Thread.currentThread());
                return "Result 2";
            });

            // Waits for the first successful result; shuts down remaining tasks
            String firstResult = scope.join();

            System.out.println("First completed result: " + firstResult);
            assertThat(firstResult).isEqualTo("Result 1"); // Task 1 wins the race

            assertThatThrownBy(task2::get) // This will throw IllegalStateException, because Task 2 was cancelled before it completed
                    .isInstanceOf(IllegalStateException.class);
        }
    }

}
