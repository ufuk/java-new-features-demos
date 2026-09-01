package io.github.ufuk.java22;

import io.github.ufuk.java22.examples.VerifiedUserAccount;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Gatherers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/// Further readings:
/// - [JDK 22 Release Notes](https://www.oracle.com/java/technologies/javase/22all-relnotes.html)
/// - [Java 22 Features (with Examples)](https://www.happycoders.eu/java/java-22-features/)
/// - [Java 22 Overview](https://www.baeldung.com/java-22-overview)
/// - [Java 22 Release Notes Review - Inside Java Newscast #65](https://www.youtube.com/watch?v=T64dUc0wIZ0)
class Java22Tests {

    @Test
    void statements_before_super_allows_validation_and_argument_preparation_prior_to_super_constructor() { // preview in Java 22, released in Java 25
        // Prior to Java 22, super(...) had to be strictly the very first statement in a constructor.
        // Starting with JEP 447 (preview in Java 22), validation and transformations can run before super(...).
        VerifiedUserAccount account = new VerifiedUserAccount("  JohnDoe  ", "  JOHNDOE@example.com  ", "12345678901");

        assertThat(account.getUsername()).isEqualTo("johndoe");
        assertThat(account.getEmail()).isEqualTo("johndoe@example.com");
        assertThat(account.getNationalId()).isEqualTo("12345678901");

        assertThrows(IllegalArgumentException.class, () ->
                new VerifiedUserAccount("alice", "invalid-email", "12345678901"));
        assertThrows(IllegalArgumentException.class, () ->
                new VerifiedUserAccount("bob", "bob@example.com", "short-id"));
    }

    @Test
    void stream_gatherers_fixed_window_for_batching_items() { // preview in Java 22, released in Java 24
        // WindowFixed groups elements into fixed-size lists (batches)
        List<String> logs = List.of("log1", "log2", "log3", "log4", "log5", "log6", "log7");

        List<List<String>> batches = logs.stream()
                .gather(Gatherers.windowFixed(3))
                .toList();

        assertThat(batches).containsExactly(
                List.of("log1", "log2", "log3"),
                List.of("log4", "log5", "log6"),
                List.of("log7")
        );
    }

    @Test
    void stream_gatherers_sliding_window_for_moving_pairwise_calculations() { // preview in Java 22, released in Java 24
        // WindowSliding creates overlapping windows of a specified size
        List<Integer> dailyTemperatures = List.of(20, 23, 19, 25, 22);

        List<List<Integer>> consecutivePairs = dailyTemperatures.stream()
                .gather(Gatherers.windowSliding(2))
                .toList();

        assertThat(consecutivePairs).containsExactly(
                List.of(20, 23),
                List.of(23, 19),
                List.of(19, 25),
                List.of(25, 22)
        );

        // Calculate differences between consecutive days
        List<Integer> tempDeltas = dailyTemperatures.stream()
                .gather(Gatherers.windowSliding(2))
                .map(pair -> pair.get(1) - pair.get(0))
                .toList();

        assertThat(tempDeltas).containsExactly(3, -4, 6, -3);
    }

    @Test
    void stream_gatherers_scan_for_cumulative_running_balance() { // preview in Java 22, released in Java 24
        // Scan performs a running accumulation, emitting each intermediate state
        List<Integer> dailyTransactions = List.of(100, -30, 50, -20, 200);

        List<Integer> runningBalance = dailyTransactions.stream()
                .gather(Gatherers.scan(() -> 0, Integer::sum))
                .toList();

        assertThat(runningBalance).containsExactly(100, 70, 120, 100, 300);
    }

    @Test
    void stream_gatherers_fold_for_custom_accumulation() { // preview in Java 22, released in Java 24
        // Fold performs an ordered reduction with a custom accumulator, emitting a single final result
        List<String> words = List.of("Java", "22", "Stream", "Gatherers");

        List<String> joinedResult = words.stream()
                .gather(Gatherers.fold(() -> "", (acc, word) -> acc.isEmpty() ? word : acc + " -> " + word))
                .toList();

        assertThat(joinedResult).containsExactly("Java -> 22 -> Stream -> Gatherers");
    }

}
