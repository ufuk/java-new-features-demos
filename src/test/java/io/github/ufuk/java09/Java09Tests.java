package io.github.ufuk.java09;

import io.github.ufuk.java09.examples.IntegerSubscriber;
import io.github.ufuk.java09.examples.MyInterface;
import io.github.ufuk.java09.examples.MyInterfaceImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/// Further readings:
/// - [JDK 9 Release Notes](https://www.oracle.com/java/technologies/javase/9-all-relnotes.html)
/// - [New Features in Java 9](https://www.baeldung.com/new-java-9)
class Java09Tests {

    @Test
    void optional_if_present_or_else() {
        Optional<Object> anEmptyOptional = Optional.empty();

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> {
                    anEmptyOptional.ifPresentOrElse(
                            System.out::println, () -> {
                                throw new RuntimeException("Expected, but no value presents"); // throws exception
                            }
                    );
                }
        );

        assertThat(e).hasMessage("Expected, but no value presents");
    }

    @Test
    void try_with_resources_external_autocloseable_instance_if_effectively_final() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample-input.txt");
        try (inputStream) {
            String fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void interface_private_methods_can_be_called_by_only_in_default_methods() {
        MyInterface anImplementation = new MyInterfaceImpl();

        anImplementation.doSomethingByDefaultUsingPrivateInterfaceMethod();
    }

    @Test
    void create_immutable_sets_with_new_of_utility() {
        Set<String> aSet = Set.of("1", "2", "3");

        assertThat(aSet).hasSize(3).contains("1", "2", "3");
        assertThrows(UnsupportedOperationException.class, () -> aSet.add("4"));
    }

    @Test
    void create_immutable_lists_with_new_of_utility() {
        List<String> aList = List.of("1", "2", "3");

        assertThat(aList).hasSize(3).containsExactly("1", "2", "3");
        assertThrows(UnsupportedOperationException.class, () -> aList.add("4"));
    }

    @Test
    void create_immutable_maps_with_new_of_utility() {
        Map<String, String> aMap = Map.of("key1", "value1", "key2", "value2");

        assertThat(aMap).hasSize(2).containsEntry("key1", "value1");
        assertThrows(UnsupportedOperationException.class, () -> aMap.put("key3", "value3"));
    }

    @Test
    void stream_operations_over_optional_item_collections_with_optional_stream() {
        List<Optional<String>> optionals = List.of(Optional.of("1"), Optional.empty(), Optional.of("3"));

        List<String> presentValues = optionals.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        assertThat(presentValues).containsExactly("1", "3");
    }

    @Test
    void publish_subscribe_framework() throws Exception {
        // Creates a publisher, which asynchronously sends submitted items to current subscribers until it is closed
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        // Creates a subscriber
        Flow.Subscriber<Integer> subscriber1 = new IntegerSubscriber();
        Flow.Subscriber<Integer> subscriber2 = new IntegerSubscriber();
        Flow.Subscriber<Integer> subscriber3 = new IntegerSubscriber();

        // Subscribes the subscriber to the publisher
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);
        publisher.subscribe(subscriber3);

        // Publishes items
        int[] numbers = {1, 2, 3, 4, 5};
        for (int number : numbers) {
            publisher.submit(number);
        }

        // Closes the publisher
        publisher.close();
    }

    @Test
    void stream_take_while_and_drop_while() {
        List<Integer> numbers = List.of(2, 4, 6, 7, 8, 10);

        List<Integer> evenPrefix = numbers.stream()
                .takeWhile(n -> n % 2 == 0)
                .collect(Collectors.toList());
        assertThat(evenPrefix).containsExactly(2, 4, 6);

        List<Integer> remainderAfterOdd = numbers.stream()
                .dropWhile(n -> n % 2 == 0)
                .collect(Collectors.toList());
        assertThat(remainderAfterOdd).containsExactly(7, 8, 10);
    }

    @Test
    void stream_iterate_with_predicate() {
        // Stream.iterate with 3 arguments mimics a classic for-loop: for (int i = 1; i <= 5; i++)
        List<Integer> numbers = Stream.iterate(1, i -> i <= 5, i -> i + 1)
                .collect(Collectors.toList());

        assertThat(numbers).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void create_immutable_maps_with_map_of_entries() {
        Map<String, Integer> scores = Map.ofEntries(
                Map.entry("Alice", 95),
                Map.entry("Bob", 88),
                Map.entry("Charlie", 72)
        );

        assertThat(scores).hasSize(3).containsEntry("Alice", 95);
    }

    @Test
    void process_handle_inspecting_current_process() {
        ProcessHandle currentProcess = ProcessHandle.current();
        ProcessHandle.Info processInfo = currentProcess.info();

        System.out.println("PID: " + currentProcess.pid());
        System.out.println("User: " + processInfo.user().orElse("N/A"));
        System.out.println("Command: " + processInfo.command().orElse("N/A"));
        System.out.println("Start Instant: " + processInfo.startInstant().orElse(null));
        System.out.println("Total CPU Duration: " + processInfo.totalCpuDuration().orElse(null));

        assertThat(currentProcess.pid()).isPositive();
        assertThat(currentProcess.isAlive()).isTrue();
        processInfo.command().ifPresent(command ->
                assertThat(command).containsIgnoringCase("java")
        );
    }

    @Test
    void completable_future_timeout_handling() {
        // completeOnTimeout provides a default fallback value if not completed within the timeout duration
        CompletableFuture<String> slowService = new CompletableFuture<>();
        CompletableFuture<String> withDefaultFallback = slowService.completeOnTimeout("Fallback Response", 50, TimeUnit.MILLISECONDS);

        assertThat(withDefaultFallback.join()).isEqualTo("Fallback Response");

        // orTimeout completes exceptionally with TimeoutException if taking longer than duration
        CompletableFuture<String> anotherSlowService = new CompletableFuture<>();
        CompletableFuture<String> withTimeoutException = anotherSlowService.orTimeout(50, TimeUnit.MILLISECONDS);

        assertThrows(CompletionException.class, withTimeoutException::join);
    }

}
