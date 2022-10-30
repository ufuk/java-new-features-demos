package io.github.ufuk.java12;

import io.github.ufuk.java12.examples.MyEnum;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Further readings:
 * - https://www.baeldung.com/java-12-new-features
 */
class Java12Tests {

    @Test
    void string_indentation() {
        String anIndentedString = "Hello\nindent method".indent(4);

        assertThat(anIndentedString).isEqualTo("    Hello\n" + "    indent method\n");
    }

    @Test
    void string_transform() {
        Function<String, String> myTurkishUpperCaseTransformer = (text) -> text.toUpperCase(Locale.of("tr", "TR")); // Locale.of exists since Java 19

        String aTransformedString = "Fıstıkçı Şahap".transform(myTurkishUpperCaseTransformer);

        assertThat(aTransformedString).isEqualTo("FISTIKÇI ŞAHAP");
    }

    @Test
    void check_if_two_files_have_different_content() throws IOException {
        Path pathToTestFile1 = Paths.get("src/test/resources/test1.txt");
        Path pathToTestFile2 = Paths.get("src/test/resources/test2.txt");

        long positionOfTheFirstMismatch = Files.mismatch(pathToTestFile1, pathToTestFile2);
        if (positionOfTheFirstMismatch > -1) {
            System.out.println("File contents mismatched!");
        }
    }

    @Test
    void compact_number_formating() {
        NumberFormat shortStyle = NumberFormat.getCompactNumberInstance(Locale.of("en", "US"), NumberFormat.Style.SHORT);
        shortStyle.setMaximumFractionDigits(0);
        assertThat(shortStyle.format(12345)).isEqualTo("12K");

        NumberFormat longStyle = NumberFormat.getCompactNumberInstance(Locale.of("en", "US"), NumberFormat.Style.LONG);
        longStyle.setMaximumFractionDigits(0);
        assertThat(longStyle.format(12345)).isEqualTo("12 thousand");

        NumberFormat longStyleTurkish = NumberFormat.getCompactNumberInstance(Locale.of("tr", "TR"), NumberFormat.Style.LONG);
        longStyleTurkish.setMaximumFractionDigits(0);
        assertThat(longStyleTurkish.format(12345)).isEqualTo("12 bin");
    }

    @Test
    void no_need_to_break_statement_with_new_shiny_lambda_like_switch_expresions() { // previewed in Java 12, released in Java 14
        // before
        String message1 = "hello";

        switch (message1) {
            case "hello":
                System.out.println("hi");
                break;
            case "goodbye":
                System.out.println("see you");
                break;
        }

        // after
        String message2 = "goodbye";

        switch (message2) {
            case "hello" -> System.out.println("hi");
            case "goodbye" -> System.out.println("see you");
        }
    }

    @Test
    void assign_the_result_of_switch() {
        MyEnum message = MyEnum.HELLO;

        String response = switch (message) { // cases must cover all possible values or add a defualt case
            case HELLO -> "hi";
            case GOOD_BYE, LATER -> "see you";
            default -> "nothing to say"; // default is redundant for this example
        };

        System.out.println(response);
    }

    @Test
    void no_need_to_explicit_casting_with_pattern_matching_support_for_instanceof() {
        // before
        Object aTextButObject = "Boilerplate";
        if (aTextButObject instanceof String) {
            String afterCasting = (String) aTextButObject;
            System.out.println(afterCasting.length());
        }

        // after
        if (aTextButObject instanceof String afterCasting) {
            System.out.println(afterCasting.length());
        }

        // more after
        if (aTextButObject instanceof String afterCasting && !afterCasting.isBlank()) {
            System.out.println(afterCasting.length());
        }
    }

}
