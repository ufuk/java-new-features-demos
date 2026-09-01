package io.github.ufuk.java14;

import io.github.ufuk.java14.examples.MyImmutableDataModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/// Further readings:
/// - [JDK 14 Release Notes](https://www.oracle.com/java/technologies/javase/14all-relnotes.html)
/// - [New Features in Java 14](https://www.baeldung.com/java-14-new-features)
class Java14Tests {

    @Test
    void text_block_multiline_but_actually_single_line() { // preview in Java 14, released in Java 15
        String singleLine = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";

        String textBlockedSingleLine = """
                Lorem Ipsum is simply dummy \
                text of the printing \
                and typesetting industry.""";

        assertThat(singleLine).isEqualTo(textBlockedSingleLine);
    }

    @Test
    void immutable_data_models_with_records() { // preview in Java 14, released in Java 16
        var lucky = new MyImmutableDataModel("13", "Lucky");

        System.out.println(lucky.id());
        System.out.println(lucky.name());
        System.out.println(lucky.toString());
        System.out.println(lucky.hashCode());

        assertThat(lucky.id()).isEqualTo("13");
        assertThat(lucky.name()).isEqualTo("Lucky");
        assertThat(lucky).isEqualTo(new MyImmutableDataModel("13", "Lucky"));
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

    @Test
    void no_need_to_explicit_casting_with_pattern_matching_support_for_instanceof() { // preview in Java 14, released in Java 16
        // before
        Object aTextButObject = "Boilerplate";
        int lengthBefore = 0;
        if (aTextButObject instanceof String) {
            String afterCasting = (String) aTextButObject;
            lengthBefore = afterCasting.length();
        }
        assertThat(lengthBefore).isEqualTo(11);

        // after
        int lengthAfter = 0;
        if (aTextButObject instanceof String afterCasting) {
            lengthAfter = afterCasting.length();
        }
        assertThat(lengthAfter).isEqualTo(11);

        // more after (pattern variable is immediately in scope for subsequent conditional checks)
        String upperText = null;
        if (aTextButObject instanceof String afterCasting && !afterCasting.isBlank()) {
            upperText = afterCasting.toUpperCase();
        }
        assertThat(upperText).isEqualTo("BOILERPLATE");
    }

}
