package io.github.ufuk.java15;

import io.github.ufuk.java15.examples.Animal;
import io.github.ufuk.java15.examples.Cat;
import io.github.ufuk.java15.examples.Dog;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 15 Release Notes](https://www.oracle.com/java/technologies/javase/15all-relnotes.html)
/// - [New Features in Java 15](https://www.baeldung.com/java-15-new)
class Java15Tests {

    @Test
    void sealed_classes_can_be_inherited_by_permitted_classes() { // preview in Java 15, released in Java 17
        // Animal permits only Cat and Dog — any other subclass is a compile-time error.
        // Both Cat and Dog are declared `final`: neither can be subclassed further.
        // Alternatively, a permitted subclass can be `non-sealed` (reopening extension to anyone)
        // or `sealed` itself (restricting further to another explicit permits list).
        //
        // The key benefit of sealed classes — exhaustive switch without a `default` —
        // is demonstrated in Java17Tests#pattern_matching_for_switch_with_sealed_types_is_exhaustive_without_default, since the
        // `case Type t ->` pattern matching syntax was first introduced in Java 17.
        Animal cat = new Cat();
        Animal dog = new Dog();

        assertThat(cat).isInstanceOf(Animal.class);
        assertThat(dog).isInstanceOf(Animal.class);
    }

}
