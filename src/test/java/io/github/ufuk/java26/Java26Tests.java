package io.github.ufuk.java26;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private static final LazyConstant<String> DB_CONFIG = LazyConstant.of(() -> {
        System.out.println("Expensive initialization running once...");
        return "jdbc:postgresql://localhost:5432/mydb";
    });

    @Test
    void lazy_constants_deferred_initialization_with_constant_folding() { // preview in Java 25 (as StableValue), changed in Java 26, released in Java ?
        // JEP 526 in Java 26 evolved JEP 502 (Stable Values from Java 25) into LazyConstant.
        // Declaring LazyConstant in a `static final` (or `final`) field solves the classic dilemma:
        // fields are not eagerly initialized at class-loading/construction time, yet the JVM can
        // still apply constant-folding optimizations as if it were a standard final constant.

        // The supplier is invoked on the first call to .get()
        String firstCall = DB_CONFIG.get();
        System.out.println("First call returned: " + firstCall);

        // Subsequent calls return the cached constant directly without re-invoking the supplier
        String secondCall = DB_CONFIG.get();
        System.out.println("Second call returned: " + secondCall);

        assertThat(firstCall)
                .isEqualTo("jdbc:postgresql://localhost:5432/mydb")
                .isEqualTo(secondCall);
    }

    @Test
    void lazy_collections_with_list_and_map_factories() { // preview in Java 26, released in Java ?
        // JEP 526 in Java 26 also adds first-class lazy collection factory methods on List and Map.
        // Elements/values are evaluated on-demand on first access and cached as constants.

        // 1. Lazy List: creates an unmodifiable list of specified size with on-demand element computation
        List<String> lazyList = List.ofLazy(3, index -> "Item-" + (index * 10));
        assertThat(lazyList).hasSize(3);
        assertThat(lazyList.get(0)).isEqualTo("Item-0");
        assertThat(lazyList.get(2)).isEqualTo("Item-20");

        // 2. Lazy Map: creates an unmodifiable map where values are computed on-demand per key
        Set<String> keys = Set.of("US", "TR", "DE");
        Map<String, String> lazyMap = Map.ofLazy(keys, countryCode -> switch (countryCode) {
            case "US" -> "United States";
            case "TR" -> "Turkey";
            case "DE" -> "Germany";
            default -> "Unknown";
        });

        assertThat(lazyMap.get("TR")).isEqualTo("Turkey");
        assertThat(lazyMap.get("US")).isEqualTo("United States");
        assertThat(lazyMap.get("DE")).isEqualTo("Germany");
    }

}
