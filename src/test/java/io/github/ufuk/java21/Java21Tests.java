package io.github.ufuk.java21;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Further readings:
 * - https://www.oracle.com/java/technologies/javase/21-relnote-issues.html
 * - https://www.happycoders.eu/java/java-21-features/
 */
class Java21Tests {

    @ParameterizedTest
    @MethodSource("provideSequencedCollections")
    void sequenced_collections(SequencedCollection<Integer> sequencedCollection) {
        // at first, contains only 1
        assertThat(sequencedCollection).hasSize(1).contains(1);
        assertThat(sequencedCollection.getFirst()).isEqualTo(1);
        assertThat(sequencedCollection.getLast()).isEqualTo(1);

        // add to start
        sequencedCollection.addFirst(0);
        // add to end
        sequencedCollection.addLast(2);
        assertThat(sequencedCollection).hasSize(3).containsSequence(0, 1, 2);

        // get first item
        assertThat(sequencedCollection.getFirst()).isEqualTo(0);

        // get first item
        assertThat(sequencedCollection.getLast()).isEqualTo(2);

        // get reversed collection
        assertThat(sequencedCollection.reversed()).hasSize(3).containsSequence(2, 1, 0);

        // remove from first
        Integer first = sequencedCollection.removeFirst();
        // remove from last
        Integer last = sequencedCollection.removeLast();
        assertThat(first).isEqualTo(0);
        assertThat(last).isEqualTo(2);
        assertThat(sequencedCollection).hasSize(1).contains(1);
    }

    static Stream<Arguments> provideSequencedCollections() {
        return Stream.of(
                Arguments.of(new ArrayList<>(List.of(1))),
                Arguments.of(new LinkedList<>(List.of(1))),
                Arguments.of(new LinkedHashSet<>(List.of(1)))
                /*
                This is also SequencedCollection, but it does not support first/last item modification
                // Arguments.of(new TreeSet<>(List.of(1)))
                 */
        );
    }

    @ParameterizedTest
    @MethodSource("provideSequencedMaps")
    void sequenced_maps(SequencedMap<String, Integer> sequencedMap) {
        // at first, contains only 1
        assertThat(sequencedMap).hasSize(1).containsEntry("one", 1);
        assertThat(sequencedMap.firstEntry().getKey()).isEqualTo("one");
        assertThat(sequencedMap.firstEntry().getValue()).isEqualTo(1);
        assertThat(sequencedMap.lastEntry().getKey()).isEqualTo("one");
        assertThat(sequencedMap.lastEntry().getValue()).isEqualTo(1);

        // add to start
        sequencedMap.putFirst("zero", 0);
        // add to end
        sequencedMap.putLast("two", 2);
        assertThat(sequencedMap.sequencedKeySet()).hasSize(3).containsSequence("zero", "one", "two");
        assertThat(sequencedMap.sequencedValues()).hasSize(3).containsSequence(0, 1, 2);
        assertThat(sequencedMap.sequencedEntrySet()).hasSize(3).containsSequence(Map.entry("zero", 0), Map.entry("one", 1), Map.entry("two", 2));

        // get first entry
        assertThat(sequencedMap.firstEntry().getKey()).isEqualTo("zero");
        assertThat(sequencedMap.firstEntry().getValue()).isEqualTo(0);
        assertThat(sequencedMap.sequencedKeySet().getFirst()).isEqualTo("zero");
        assertThat(sequencedMap.sequencedValues().getFirst()).isEqualTo(0);

        // get last entry
        assertThat(sequencedMap.lastEntry().getKey()).isEqualTo("two");
        assertThat(sequencedMap.lastEntry().getValue()).isEqualTo(2);
        assertThat(sequencedMap.sequencedKeySet().getLast()).isEqualTo("two");
        assertThat(sequencedMap.sequencedValues().getLast()).isEqualTo(2);

        // get reversed entry
        assertThat(sequencedMap.reversed().sequencedKeySet()).hasSize(3).containsSequence("two", "one", "zero");
        assertThat(sequencedMap.reversed().sequencedValues()).hasSize(3).containsSequence(2, 1, 0);
        assertThat(sequencedMap.reversed().sequencedEntrySet()).hasSize(3).containsSequence(Map.entry("two", 2), Map.entry("one", 1), Map.entry("zero", 0));

        // poll from start
        Map.Entry<String, Integer> firstEntry = sequencedMap.pollFirstEntry();
        // poll from end
        Map.Entry<String, Integer> lastEntry = sequencedMap.pollLastEntry();
        assertThat(firstEntry.getKey()).isEqualTo("zero");
        assertThat(firstEntry.getValue()).isEqualTo(0);
        assertThat(lastEntry.getKey()).isEqualTo("two");
        assertThat(lastEntry.getValue()).isEqualTo(2);
        assertThat(sequencedMap.sequencedEntrySet()).hasSize(1).containsSequence(Map.entry("one", 1));
    }

    static Stream<Arguments> provideSequencedMaps() {
        return Stream.of(
                Arguments.of(new LinkedHashMap<>(Map.of("one", 1)))
                /*
                These are also SequencedMap, but they do not support first/last entry modification
                // Arguments.of(new ConcurrentSkipListMap<>(Map.of("one", 1))),
                // Arguments.of(new TreeMap<>(Map.of("one", 1)))
                 */
        );
    }

    @Test
    void new_string_method_for_search_substring_in_range() {
        String text = "My dog's name is Roxy, it is 3 years old Labrador. It is a very playful dog.";

        int resultIndex = text.indexOf("is", 20, 30);

        assertThat(resultIndex).isEqualTo(26);
    }

    @Test
    void new_string_method_for_search_a_single_character_in_range() {
        String text = "My dog's name is Roxy, it is 3 years old Labrador. It is a very playful dog.";

        int resultIndex = text.indexOf('s', 20, 30);

        assertThat(resultIndex).isEqualTo(27);
    }

    @Test
    void repeat_method_for_string_builder_and_string_buffer() {
        String text = new StringBuilder()
                .append("I like to")
                .repeat(" move it", 2)
                .append("\n")
                .append("Ya like to (move it!)")
                .toString();

        assertThat(text).isEqualTo("""
                I like to move it move it
                Ya like to (move it!)""");
    }

    @Test
    void squeeze_values_between_two_values() {
        int cannotLessThan5 = Math.clamp(4, 5, 10);
        assertThat(cannotLessThan5).isEqualTo(5);

        int cannotGreaterThan10 = Math.clamp(11, 5, 10);
        assertThat(cannotGreaterThan10).isEqualTo(10);

        int goodBoy = Math.clamp(6, 5, 10);
        assertThat(goodBoy).isEqualTo(6);
    }

    /* dropped in Java 23
    @Test
    void string_templates() { // preview in Java 21
        String name = "Roxy";
        String breed = "Labrador";
        int yearOfBirth = 2020;

        String dog = STR."My dog's name is \{name}, it is \{calculateAge(yearOfBirth)} years old \{breed}.";

        String dogAsUsedToBe = new StringBuilder()
                .append("My dog's name is ")
                .append(name)
                .append(", it is ")
                .append(calculateAge(yearOfBirth))
                .append(" years old ")
                .append(breed)
                .append(".")
                .toString();

        assertThat("My dog's name is Roxy, it is " + calculateAge(yearOfBirth) + " years old Labrador.")
                .isEqualTo(dog)
                .isEqualTo(dogAsUsedToBe);
    }

    static int calculateAge(int yearOfBirth) {
        return LocalDate.now().getYear() - yearOfBirth;
    }
    */

    // Defines a ScopedValue for username
    static final ScopedValue<String> SV_USERNAME = ScopedValue.newInstance();

    @Test
    void scoped_values() { // preview in Java 21, released in Java ?
        // Binds a value to the SV_USERNAME within a scope
        ScopedValue.where(SV_USERNAME, "user1").run(() -> {
            // Accesses the value within the scope
            String username = SV_USERNAME.get();
            System.out.println("Username inside the scope: " + username);

            printUsernameInsideTheScope();

            // Nested scopes (shadowing) example
            ScopedValue.where(SV_USERNAME, "user2").run(() -> {
                System.out.println("Username in nested scope: " + SV_USERNAME.get());
                printUsernameInsideTheScope();
            });

            System.out.println("Username after nested scope: " + SV_USERNAME.get());
        });

        // Attempting to access the scoped value outside the scope
        try {
            SV_USERNAME.get();
        } catch (NoSuchElementException e) {
            System.out.println("Failed to access!");
        }

        // Example of null value scope
        ScopedValue.where(SV_USERNAME, null).run(() -> {
            String username = SV_USERNAME.get();
            System.out.println("Username inside the scope: " + username);
        });
    }

    // Another method that accesses the same scoped value
    void printUsernameInsideTheScope() {
        String username = SV_USERNAME.get();
        System.out.println("Username inside the scope (from another method): " + username);
    }

    @Test
    void structured_concurrency_when_all_tasks_successfully_completed() throws Exception { // preview in Java 21, released in Java ?
        try (var structuredTaskScope = new StructuredTaskScope<String>()) {
            var subTask1 = structuredTaskScope.fork(() -> {
                System.out.println("Task 1 started: " + Thread.currentThread()); // As you may have noticed from the outputs, these threads are "lightweight" virtual threads in a ForkJoinPool
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println("Task 1 finished: " + Thread.currentThread());
                return "Result 1";
            });

            var subTask2 = structuredTaskScope.fork(() -> {
                System.out.println("Task 2 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofSeconds(2));
                System.out.println("Task 2 finished: " + Thread.currentThread());
                return "Result 2";
            });

            // Waits for tasks to complete
            structuredTaskScope.join();

            // Gets results
            System.out.println("Result of Task 1: " + subTask1.get());
            System.out.println("Result of Task 2: " + subTask2.get());
        }
    }

    @Test
    void structured_concurrency_when_some_tasks_failed() throws Exception { // preview in Java 21, released in Java ?
        try (var structuredTaskScope = new StructuredTaskScope<String>()) {
            var subTask1 = structuredTaskScope.fork(() -> {
                System.out.println("Task 1 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofSeconds(1));
                throw new RuntimeException("Task 1 failed!");
                // return "Result 1";
            });

            var subTask2 = structuredTaskScope.fork(() -> {
                System.out.println("Task 2 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofSeconds(2));
                System.out.println("Task 2 finished: " + Thread.currentThread());
                return "Result 2";
            });

            // Waits for tasks to complete
            structuredTaskScope.join();

            // Attempts to get results
            System.out.println("Result of Task 2: " + subTask2.get()); // This will get result even if Task 1 completed before Task 2
            System.out.println("Result of Task 1: " + subTask1.get()); // This will throw IllegalStateException, because Task 1 failed
        } catch (IllegalStateException e) {
            System.out.println("Couldn't get result for Task 1: " + e.getMessage());
        }
    }

    @Test
    void structured_concurrency_when_all_tasks_successfully_completed_but_shutdown_on_success() throws Exception { // preview in Java 21, released in Java ?
        try (var structuredTaskScope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            var subTask1 = structuredTaskScope.fork(() -> {
                System.out.println("Task 1 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println("Task 1 finished: " + Thread.currentThread());
                return "Result 1";
            });

            var subTask2 = structuredTaskScope.fork(() -> {
                System.out.println("Task 2 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofSeconds(2));
                System.out.println("Task 2 finished: " + Thread.currentThread());
                return "Result 2";
            });

            // Waits for tasks to complete
            structuredTaskScope.join();

            // Attempts to get results
            System.out.println("Result of Task 1: " + subTask1.get());
            System.out.println("Result of Task 2: " + subTask2.get()); // This will throw IllegalStateException, because Task 1 completed before Task 2
        } catch (IllegalStateException e) {
            System.out.println("Couldn't get result for Task 2: " + e.getMessage());
        }
    }

    @Test
    void structured_concurrency_when_some_tasks_failed_but_shutdown_on_failure() throws Exception { // preview in Java 21, released in Java ?
        try (var structuredTaskScope = new StructuredTaskScope.ShutdownOnFailure()) {
            var subTask0 = structuredTaskScope.fork(() -> {
                System.out.println("Task 0 started: " + Thread.currentThread());
                System.out.println("Task 0 finished: " + Thread.currentThread());
                return "Result 0";
            });

            var subTask1 = structuredTaskScope.fork(() -> {
                System.out.println("Task 1 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofSeconds(2));
                System.out.println("Task 1 finished: " + Thread.currentThread());
                return "Result 1";
            });

            var subTask2 = structuredTaskScope.fork(() -> {
                System.out.println("Task 2 started: " + Thread.currentThread());
                Thread.sleep(Duration.ofSeconds(1));
                throw new RuntimeException("Task 2 failed!");
                // return "Result 2";
            });

            // Waits for tasks to complete
            structuredTaskScope.join();

            // Attempts to get results
            System.out.println("Result of Task 0: " + subTask0.get()); // This will get result
            System.out.println("Result of Task 1: " + subTask1.get()); // This will throw IllegalStateException, because Task 2 failed before Task 1 is completed
            // System.out.println("Result of Task 2: " + subTask2.get());
        } catch (IllegalStateException e) {
            System.out.println("Couldn't get results of Task 1 and Task 2: " + e.getMessage());
        }
    }

    @Test
    void unnamed_class_and_main_method() { // preview in Java 21, released in Java ?
        /*
        Run command on terminal:
        java --enable-preview --source 21 src/test/java/io/github/ufuk/java21/examples/SimplerMain.java

        Expected output:
        Note: src/test/java/io/github/ufuk/java21/examples/SimplerMain.java uses preview features of Java SE 21.
        Note: Recompile with -Xlint:preview for details.
        Bye-bye boilerplate!
         */
    }

    @Test
    void unnamed_variables() { // preview in Java 21, released in Java ?
        Assertions.fail("No example presents"); // TODO: add example(s)
    }

    @Test
    void unnamed_patterns() { // preview in Java 21, released in Java ?
        Assertions.fail("No example presents"); // TODO: add example(s)
    }

}
