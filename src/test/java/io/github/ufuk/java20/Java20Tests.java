package io.github.ufuk.java20;

import io.github.ufuk.java17.examples.Cat;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 20 Release Notes](https://www.oracle.com/java/technologies/javase/20all-relnotes.html)
/// - [Java 20 Features (with Examples)](https://www.happycoders.eu/java/java-20-features/)
/// - [New Features in Java 20](https://www.baeldung.com/java-20-new-features)
class Java20Tests {

    @Test
    void using_loop_and_record_patterns() { // preview in Java 20 (JEP 432), dropped in Java 21 (JEP 440)
        List<Cat> cats = List.of(new Cat("Sıdıka"), new Cat("Mihriban"));

        // In Java 20 (2nd preview of Record Patterns), pattern matching was supported in enhanced for-loops:
        /*
        List<String> names = new ArrayList<>();
        for (Cat(String name) : cats) {
            names.add(name);
        }
        assertThat(names).containsExactly("Sıdıka", "Mihriban");
        */

        // However, record patterns in `for` loops were dropped in Java 21 GA (JEP 440) to undergo further design review.
        // Therefore, standard for-loop deconstruction is used:
        List<String> names = new ArrayList<>();
        for (Cat cat : cats) {
            System.out.println(cat.name());
            names.add(cat.name());
        }

        assertThat(names).containsExactly("Sıdıka", "Mihriban");
    }

}
