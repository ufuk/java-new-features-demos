package io.github.ufuk.java23;

import io.github.ufuk.java23.examples.CachedReport;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 23 Release Notes](https://www.oracle.com/java/technologies/javase/23all-relnotes.html)
/// - [Java 23 Features (with Examples)](https://www.happycoders.eu/java/java-23-features/)
/// - [Reviewing the JDK 23 Release Notes - Inside Java Newscast #76](https://www.youtube.com/watch?v=IluRn8ecuCo)
class Java23Tests {

    @Test
    void no_need_to_write_class_to_say_hello_even_we_can_skip_system_dot_out_now() { // preview in Java 21, changed in Java 23, changed and released in Java 25
        /*
        Run command on terminal:
        java --enable-preview --source 23 src/test/java/io/github/ufuk/java23/examples/MoreSimplerMain.java

        Expected output:
        Bye-bye boilerplate!
        */
    }

    /// This method demonstrates Markdown documentation comments introduced in Java 23 (JEP 467).
    ///
    /// Markdown comments use `///` instead of traditional `/** ... */` blocks.
    /// Key advantages:
    /// - **No leading asterisks** required on each line
    /// - Standard Markdown syntax support (bold, italic, lists, backticks, code blocks, links)
    /// - Seamless integration with `@tag` annotations (e.g., `@see`, `@param`, `@return`)
    @Test
    void markdown_documentation_comments() { // released in Java 23
        // Markdown documentation comments are written with triple slashes (///) as demonstrated above.
        // They are processed natively by javadoc from Java 23 onwards without requiring HTML tags.
    }

    @Test
    void primitive_types_in_switch_pattern_matching() { // preview in Java 23, released in Java 25
        // Prior to Java 23, pattern matching was restricted to reference types.
        // JEP 455 allows primitive type patterns in switch statements and expressions.
        int httpStatusCode = 200;

        String category = switch (httpStatusCode) {
            case 200 -> "OK";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            case int code when code >= 200 && code < 300 -> "Successful (" + code + ")";
            case int code when code >= 400 && code < 500 -> "Client Error (" + code + ")";
            case int code when code >= 500 && code < 600 -> "Server Error (" + code + ")";
            case int code -> "Other Code: " + code;
        };

        assertThat(category).isEqualTo("OK");

        int clientErrorCode = 403;
        String clientErrorCategory = switch (clientErrorCode) {
            case 200 -> "OK";
            case int code when code >= 400 && code < 500 -> "Client Error (" + code + ")";
            default -> "Unknown";
        };

        assertThat(clientErrorCategory).isEqualTo("Client Error (403)");
    }

    @Test
    void primitive_types_in_instanceof_pattern_matching() { // preview in Java 23, released in Java 25
        // Testing whether a value fits safely into a narrower primitive type without loss
        long largeValue = 42L;

        String description;
        if (largeValue instanceof byte b) {
            description = "Fits into byte: " + b;
        } else if (largeValue instanceof short s) {
            description = "Fits into short: " + s;
        } else {
            description = "Large long: " + largeValue;
        }

        assertThat(description).isEqualTo("Fits into byte: 42");
    }

    @Test
    void flexible_constructor_bodies_assigning_subclass_fields_before_super_invocation() { // preview in Java 22, still preview in Java 23, released in Java 25
        // In Java 23 (2nd preview of Flexible Constructor Bodies - JEP 482),
        // we can initialize fields of the subclass before explicitly invoking super(...).
        CachedReport report = new CachedReport("Annual Financial Summary");

        assertThat(report.getReportId()).isNotBlank();
        assertThat(report.getGeneratedAt()).isNotNull();
        assertThat(report.getHeader()).contains("Annual Financial Summary", report.getReportId());
    }

    @Test
    void module_import_declarations() { // preview in Java 23, released in Java 25
        /*
        Module Import Declarations (JEP 476, preview in Java 23) allows importing all public
        packages exported by a module with a single declaration at the top of a file:

        import module java.base;

        This automatically imports:
        - java.util.* (List, Map, Set, Stream, Optional, UUID, Objects, etc.)
        - java.io.* (File, InputStream, OutputStream, etc.)
        - java.nio.file.* (Path, Files, Paths, etc.)
        - java.time.* (Instant, LocalDate, Duration, etc.)
        - java.util.concurrent.* (CompletableFuture, Executors, etc.)
        without having to write dozens of separate import lines!
        */
    }

}
