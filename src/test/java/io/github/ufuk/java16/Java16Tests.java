package io.github.ufuk.java16;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Further readings:
 * - https://www.baeldung.com/java-16-new-features
 */
class Java16Tests {

    @Test
    void stream_to_list() {
        List<String> aModifiableList = new ArrayList<>();
        aModifiableList.add("1");
        aModifiableList.add("2");
        aModifiableList.add("3");

        List<String> anImmutableList = aModifiableList.stream()
                .filter(StringUtils::isNumeric)
                .toList(); // it was like ".collect(Collectors.toUnmodifiableList());" before

        assertThrows(
                UnsupportedOperationException.class,
                () -> {
                    anImmutableList.add("4"); // throws exception
                }
        );
    }

    // TODO: add more examples on records with its final-release features

}
