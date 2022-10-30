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
    void pattern_mathing_for_switch() {
        Object animal = new Cat("Sıdıka");

        String name = switch (animal) {
            case Cat c -> c.name();
            case Dog d -> d.name();
            default -> "unidentified animal";
        };

        System.out.println(name);
    }

}
