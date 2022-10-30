package io.github.ufuk.java11;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Further readings:
 * - https://www.baeldung.com/java-11-new-features
 */
class Java11Tests {

    @Test
    void builtin_http_client() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest httpRequest = HttpRequest.newBuilder().GET()
                .uri(new URI("https://postman-echo.com/get"))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(httpResponse.body()).isNotBlank();
    }

    @Test
    void optional_is_empty() {
        Optional<Object> anOptional = Optional.empty();

        if (anOptional.isEmpty()) {
            System.out.println("Empty");
        }

        // before
        if (!anOptional.isPresent()) {
            System.out.println("Again, empty");
        }
    }

    @Test
    void split_string_to_lines_as_a_stream() {
        Stream<String> lines = "I\ncan\neasily\nsplit\nlines".lines();

        lines.forEach(System.out::println);
    }

    @Test
    void check_if_string_is_blank() {
        if ("        ".isBlank()) {
            System.out.println("Yes, it's blank. Nothing but spaces!");
        }

        if ("".isEmpty()) {
            System.out.println("Yes, it's empty. Empty as outer space!");
        }
    }

    @Test
    void use_strip_instead_of_trim() {
        assertThat("  A  ".strip()).isEqualTo("A");

        assertThat("  A  ".stripLeading()).isEqualTo("A  ");

        assertThat("  A  ".stripTrailing()).isEqualTo("  A");
    }

    @Test
    void write_and_read_strings_with_new_nio_utility_methods() throws IOException {
        Path pathToTestFile2 = Paths.get("src/test/resources/test2.txt");

        // writeString to file
        Files.writeString(pathToTestFile2, "Hello Test 2 from Java 11)");

        // readString from file
        String testFile2Content = Files.readString(pathToTestFile2);

        System.out.println(testFile2Content);
    }

    @Test
    void convert_collection_to_array() {
        Collection<String> aStringCollection = List.of("1", "2", "3");

        String[] aStringArray = aStringCollection.toArray(String[]::new);

        Arrays.stream(aStringArray) // convert an array to stream, then print its items
                .forEach(System.out::println);
    }

}
