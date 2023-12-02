package io.github.ufuk.java17;

import io.github.ufuk.java17.examples.Cat;
import io.github.ufuk.java17.examples.Dog;
import org.junit.jupiter.api.Test;

/**
 * Further readings:
 * - https://www.baeldung.com/java-17-new-features
 */
class Java17Tests {

    @Test
    void pattern_matching_for_switch() { // preview in Java 17, released in Java 21
        Object animal = new Cat("Sıdıka");

        String name = switch (animal) {
            // case Cat c && c.name().endsWith("a") -> c.name() + "*";
            case Cat c -> c.name() + " the cat";
            case Dog d -> d.name() + " the dog";
            default -> "unidentified animal";
        };

        System.out.println(name);
    }

}
