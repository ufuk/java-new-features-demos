package io.github.ufuk.java13;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 13 Release Notes](https://www.oracle.com/java/technologies/javase/13all-relnotes.html)
/// - [New Features in Java 13](https://www.baeldung.com/java-13-new-features)
class Java13Tests {

    @Test
    void what_if_we_want_do_something_then_return_value_in_switch_expressions_the_answer_is_yield() { // preview in Java 13, released in Java 14
        String question = "the meaning of life, the universe, and everything";

        String answer = switch (question) {
            case "2 + 2 = ?" -> "4";
            case "2 * 0 = ?", "3 * 0 = ?" -> "0";
            case "the meaning of life, the universe, and everything" -> {
                System.out.println("Calculating...");
                yield "42";
            }
            default -> {
                System.out.println("I'm aware of the wisdom of saying \"I don't know\"");
                yield "I don't know";
            }
        };

        System.out.println(answer);
        assertThat(answer).isEqualTo("42");
    }

    @Test
    void readable_multiline_text_with_text_blocks() { // preview in Java 13, released in Java 15
        // before
        String oldJsonString = "{\n    \"key\": \"value\"\n}\n";

        // after
        String textBlockJsonString = """
                {
                    "key": "value"
                }
                """;

        System.out.println(textBlockJsonString);
        assertThat(textBlockJsonString).isEqualTo(oldJsonString);
    }

}
