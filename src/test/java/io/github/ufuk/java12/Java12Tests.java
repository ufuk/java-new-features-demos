package io.github.ufuk.java12;

import io.github.ufuk.java12.examples.MyEnum;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 12 Release Notes](https://www.oracle.com/java/technologies/javase/12all-relnotes.html)
/// - [New Features in Java 12](https://www.baeldung.com/java-12-new-features)
class Java12Tests {

    @Test
    void string_indentation() {
        String anIndentedString = "Hello\nindent method".indent(4);

        assertThat(anIndentedString).isEqualTo("    Hello\n" + "    indent method\n");
    }

    @Test
    void string_transform() {
        Function<String, String> myTurkishUpperCaseTransformer = (text) -> text.toUpperCase(Locale.forLanguageTag("tr-TR"));

        String aTransformedString = "Fıstıkçı Şahap".transform(myTurkishUpperCaseTransformer);

        assertThat(aTransformedString).isEqualTo("FISTIKÇI ŞAHAP");
    }

    @Test
    void check_if_two_files_have_different_content() throws IOException {
        Path tempFile1 = Files.createTempFile("mismatch-demo-1-", ".txt");
        Path tempFile2 = Files.createTempFile("mismatch-demo-2-", ".txt");

        Files.writeString(tempFile1, "Hello World from File 1");
        Files.writeString(tempFile2, "Hello World from File 2");

        long positionOfTheFirstMismatch = Files.mismatch(tempFile1, tempFile2);
        assertThat(positionOfTheFirstMismatch).isGreaterThanOrEqualTo(0);

        System.out.println("First mismatch at index: " + positionOfTheFirstMismatch);
        System.out.println("Compared files: " + tempFile1.toAbsolutePath() + " and " + tempFile2.toAbsolutePath());
    }

    @Test
    void compact_number_formating() {
        NumberFormat shortStyle = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        shortStyle.setMaximumFractionDigits(0);
        assertThat(shortStyle.format(12345)).isEqualTo("12K");

        NumberFormat longStyle = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);
        longStyle.setMaximumFractionDigits(0);
        assertThat(longStyle.format(12345)).isEqualTo("12 thousand");

        NumberFormat longStyleTurkish = NumberFormat.getCompactNumberInstance(Locale.forLanguageTag("tr-TR"), NumberFormat.Style.LONG);
        longStyleTurkish.setMaximumFractionDigits(0);
        assertThat(longStyleTurkish.format(12345)).isEqualTo("12 bin");
    }

    @Test
    void no_need_to_break_statement_with_new_shiny_lambda_like_switch_expresions() { // preview in Java 12, released in Java 14
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
    void collectors_teeing_to_combine_two_downstream_collectors() {
        // Combines two downstream collectors (e.g. min and max, or sum and count) into a single result
        record MinMax(int min, int max) {
        }

        MinMax minMax = Stream.of(15, 3, 42, 8, 27).collect(
                Collectors.teeing(
                        Collectors.minBy(Integer::compareTo),
                        Collectors.maxBy(Integer::compareTo),
                        (minOpt, maxOpt) -> new MinMax(minOpt.orElse(0), maxOpt.orElse(0))
                )
        );

        assertThat(minMax).isEqualTo(new MinMax(3, 42));
    }

    @Test
    void completable_future_asynchronous_exception_recovery_with_exceptionally_compose() {
        // In Java 12, exceptionallyCompose allows recovering from an exception by chaining another asynchronous CompletionStage.
        // It acts like an asynchronous `catch` block — if there is no exception, it is completely bypassed.

        // 1. Success case: No exception occurs, exceptionallyCompose is bypassed and original result is preserved
        CompletableFuture<String> successfulPrimaryService = CompletableFuture.completedFuture("Primary Service OK");
        CompletableFuture<String> resultWhenSuccess = successfulPrimaryService.exceptionallyCompose(throwable ->
                CompletableFuture.supplyAsync(() -> "Backup Service Result")
        );
        assertThat(resultWhenSuccess.join()).isEqualTo("Primary Service OK");

        // 2. Failure case: Primary service fails, exceptionallyCompose triggers the asynchronous backup service
        CompletableFuture<String> failingPrimaryService = CompletableFuture.failedFuture(new RuntimeException("Primary Service Down"));
        CompletableFuture<String> resultWhenFailed = failingPrimaryService.exceptionallyCompose(throwable ->
                CompletableFuture.supplyAsync(() -> "Recovered from Backup Service (" + throwable.getMessage() + ")")
        );
        assertThat(resultWhenFailed.join()).isEqualTo("Recovered from Backup Service (Primary Service Down)");
    }

}
