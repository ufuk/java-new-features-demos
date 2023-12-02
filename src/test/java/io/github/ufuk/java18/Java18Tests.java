package io.github.ufuk.java18;

import io.github.ufuk.java18.examples.MySimpleWebServer;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Further readings:
 * - https://www.oracle.com/java/technologies/javase/18-relnote-issues.html
 * - https://openjdk.org/jeps/408
 */
class Java18Tests {

    @Test
    void from_now_on_default_charset_is_utf8() {
        Charset defaultCharset = Charset.defaultCharset();

        assertThat(defaultCharset.name()).isEqualTo("UTF-8");
    }

    @Test
    void out_of_the_box_static_http_file_server() throws Exception {
        // Initialize Simple Web Server
        MySimpleWebServer.initializeServer(8080);

        // Request to server and verify response
        var httpClient = HttpClient.newBuilder().build();
        var httpRequest = HttpRequest.newBuilder().GET()
                .uri(new URI("http://localhost:8080/index.html"))
                .build();
        var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(httpResponse.body()).contains("<h1>Hello, World!</h1>");
    }

}
