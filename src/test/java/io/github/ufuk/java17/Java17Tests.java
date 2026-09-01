package io.github.ufuk.java17;

import io.github.ufuk.java17.examples.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 17 Release Notes](https://www.oracle.com/java/technologies/javase/17all-relnotes.html)
/// - [New Features in Java 17](https://www.baeldung.com/java-17-new-features)
class Java17Tests {

    @Test
    void pattern_matching_for_switch() { // preview in Java 17, released in Java 21
        Object animal = new Cat("Sıdıka");

        String name = switch (animal) {
            // The `&&` guard syntax was used in the early previews (Java 17-18).
            // It was replaced by the `when` keyword from Java 19 onwards — see Java19Tests.
            // case Cat c && c.name().endsWith("a") -> c.name() + "*";
            case Cat c -> c.name() + " the cat";
            case Dog d -> d.name() + " the dog";
            default -> "unidentified animal";
        };

        System.out.println(name);
        assertThat(name).isEqualTo("Sıdıka the cat");
    }

    @Test
    void pattern_matching_for_switch_with_sealed_types_is_exhaustive_without_default() { // preview in Java 17, released in Java 21
        // When switching over a sealed hierarchy, the compiler knows all possible subtypes.
        // Therefore, the pattern matching switch is exhaustive and does not require a `default` branch.
        Shape shape = new Circle(5.0);

        String description = switch (shape) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle with area " + (r.width() * r.height());
            // No default branch needed: Shape only permits Circle and Rectangle
        };

        System.out.println(description);
        assertThat(description).isEqualTo("Circle with radius 5.0");
    }

}
