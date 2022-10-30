package io.github.ufuk.java10;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Further readings:
 * - https://www.baeldung.com/java-10-overview
 * - https://www.baeldung.com/java-10-local-variable-type-inference
 * - https://www.baeldung.com/java-10-performance-improvements
 */
class Java10Tests {

    @Test
    void local_variable_type_inference() {
        // before
        String oldLocalText = "Hello var";

        Map<String, String> anOldLocalMap = Map.of("key", "value");

        // after
        var newLocalText = "Hello var";

        var aNewLocalMap = Map.of("key", "value");
    }

    @Test
    void create_immutable_lists_with_new_copy_of_utility() { // for sets and maps; use "Set.copyOf(...)" or "Map.copyOf(...)"
        List<String> aModifiableList = new ArrayList<>();
        aModifiableList.add("1");
        aModifiableList.add("2");
        aModifiableList.add("3");

        List<String> anImmutableList = List.copyOf(aModifiableList);
        anImmutableList.add("4"); // throws error
    }

    @Test
    void create_immutable_lists_with_new_collector() { // for sets and maps; use "Collectors.toUnmodifiableSet()" or "Collectors.toUnmodifiableMap(...)"
        List<String> aModifiableList = new ArrayList<>();
        aModifiableList.add("1");
        aModifiableList.add("2");
        aModifiableList.add("3");

        List<String> anImmutableList = aModifiableList.stream()
                .filter(StringUtils::isNumeric)
                .collect(Collectors.toUnmodifiableList());
        anImmutableList.add("4"); // throws error
    }

    @Test
    void optional_or_else_throw_no_such_element_exception_easily() {
        Optional<String> anOptionalString = Optional.of("text");

        Long convertedValue = anOptionalString.filter(StringUtils::isNumeric)
                .map(Long::parseLong)
                .orElseThrow();
    }

}
