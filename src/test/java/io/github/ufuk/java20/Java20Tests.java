package io.github.ufuk.java20;

import io.github.ufuk.java17.examples.Cat;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Further readings:
 * - https://www.oracle.com/java/technologies/javase/20-relnote-issues.html
 * - https://www.happycoders.eu/java/java-20-features/
 */
class Java20Tests {

    @Test
    void using_loop_and_record_paterns() { // preview in 20, removed in Java 21
        List<Cat> cats = List.of(new Cat("Sıdıka"), new Cat("Mihriban"));

        for (Cat c : cats) {              // for (Cat(String name) : cats) {
            System.out.println(c.name()); //     System.out.println(name);
        }                                 // }
    }

}
