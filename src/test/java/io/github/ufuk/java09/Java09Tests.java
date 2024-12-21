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
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Further readings:
 * - https://www.baeldung.com/new-java-9
 */
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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test1.txt");
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
    }

    @Test
    void create_immutable_lists_with_new_of_utility() {
        List<String> aList = List.of("1", "2", "3");
    }

    @Test
    void create_immutable_maps_with_new_of_utility() {
        Map<String, String> aMap = Map.of("key1", "value1", "key2", "value2");
    }

    @Test
    void stream_operations_over_optional_item_collections_with_optional_stream() {
        List<Optional<String>> optionals = List.of(Optional.of("1"), Optional.empty(), Optional.of("3"));

        optionals.stream()
                .flatMap(Optional::stream)
                .forEach(System.out::println);
    }

    @Test
    void publish_subscribe_framework() throws Exception {
        // Create a publisher, which asynchronously sends submitted items to current subscribers until it is closed
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        // Create a subscriber
        Flow.Subscriber<Integer> subscriber1 = new IntegerSubscriber();
        Flow.Subscriber<Integer> subscriber2 = new IntegerSubscriber();
        Flow.Subscriber<Integer> subscriber3 = new IntegerSubscriber();

        // Subscribe the subscriber to the publisher
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);
        publisher.subscribe(subscriber3);

        // Publish items
        int[] numbers = {1, 2, 3, 4, 5};
        for (int number : numbers) {
            publisher.submit(number);
        }

        // Close the publisher
        publisher.close();
    }

}
