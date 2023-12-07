package io.github.ufuk.java21;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
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
    void string_templates() { // preview in Java 21, released in Java ?
        String name = "Roxy";
        String breed = "Labrador";
        int age = 3;

        String dog = STR.
                """
                My dog's name is \{ name }, it is \{ age } years old \{ breed }.""" ;

        String dogAsUsedToBe = new StringBuilder()
                .append("My dog's name is ")
                .append(name)
                .append(", it is ")
                .append(age)
                .append(" years old ")
                .append(breed)
                .append(".")
                .toString();

        System.out.println(dog);
        System.out.println(dogAsUsedToBe);

        Assertions.assertEquals(dog, dogAsUsedToBe);
    }

    @Test
    void todo() {
        Assertions.fail("No example presents"); // TODO: add example(s)
    }

}
