package io.github.ufuk.java23;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Further readings:
 * - https://www.oracle.com/java/technologies/javase/23-relnote-issues.html
 * - https://www.happycoders.eu/java/java-23-features/
 * - https://www.youtube.com/watch?v=IluRn8ecuCo (Reviewing the JDK 23 Release Notes - Inside Java Newscast #76)
 */
class Java23Tests {

    @Test
    void no_need_to_write_class_to_say_hello_even_we_can_skip_system_dot_out_now() { // preview in Java 21, still preview in Java 23, released in Java ?
        /*
        Run command on terminal:
        java --enable-preview --source 23 src/test/java/io/github/ufuk/java23/examples/MoreSimplerMain.java

        Expected output:
        Bye-bye boilerplate!
        */
    }

    @Test
    void todo() {
        Assertions.fail("No example presents"); // TODO: add more example(s)
    }

}
