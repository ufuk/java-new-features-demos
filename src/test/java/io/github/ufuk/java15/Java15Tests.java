package io.github.ufuk.java15;

import io.github.ufuk.java15.examples.Animal;
import io.github.ufuk.java15.examples.Cat;
import org.junit.jupiter.api.Test;

/**
 * Further readings:
 * - https://www.baeldung.com/java-15-new
 */
class Java15Tests {

    @Test
    void sealed_classes_can_be_inherited_by_permitted_classes() {
        Animal cat = new Cat();
    }

}
