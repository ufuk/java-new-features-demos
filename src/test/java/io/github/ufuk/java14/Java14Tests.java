package io.github.ufuk.java14;

import io.github.ufuk.java14.examples.MyImmutableDataModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Further readings:
 * - https://www.baeldung.com/java-14-new-features
 */
class Java14Tests {

    @Test
    void text_block_multiline_but_actually_single_line() { // previewed in Java 14, released in Java 15
        String singleLine = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";

        String textBlockedSingleLine = """
                Lorem Ipsum is simply dummy \
                text of the printing \
                and typesetting industry.""";

        assertThat(singleLine).isEqualTo(textBlockedSingleLine);
    }

    @Test
    void immutable_data_models_with_records() { // previewed in Java 14, released in Java 16
        var lucky = new MyImmutableDataModel("13", "Lucky");

        System.out.println(lucky.id());
        System.out.println(lucky.name());
        System.out.println(lucky.toString());
        System.out.println(lucky.hashCode());
    }

    @Test
    void what_exactly_was_null() {
        var data = new MyImmutableDataModel("13", null);

        NullPointerException e = assertThrows(
                NullPointerException.class,
                () -> {
                    System.out.println(data.name().length()); // throws exception
                }
        );

        assertThat(e).hasMessage("Cannot invoke \"String.length()\" because the return value of \"io.github.ufuk.java14.examples.MyImmutableDataModel.name()\" is null");
    }

}
