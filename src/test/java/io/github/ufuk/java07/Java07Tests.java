package io.github.ufuk.java07;

import io.github.ufuk.java07.examples.ForkJoinArraySumTask;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 7 Release Notes](https://www.oracle.com/java/technologies/javase/javase-jdk7-relnotes.html)
/// - [Java 7 Features and Changes](https://howtodoinjava.com/series/java-versions-features/#java-se-7-features)
class Java07Tests {

    @Test
    void improved_type_inference_with_blank_diamond() {
        // before
        Map<String, String> keyValueStore1 = new HashMap<String, String>();

        // after
        Map<String, String> keyValueStore2 = new HashMap<>();
    }

    @Test
    void before_try_with_resources() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("sample-input.txt");

            // InputStream.readAllBytes exists since Java 9
            String fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    void after_try_with_resources() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample-input.txt")) {
            String fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void after_try_with_resources_with_1_input_1_output_resource() throws IOException {
        Path tempOutputFile = Files.createTempFile("output-", ".txt");

        // Reads from the static input file and writes a transformed version to a temp output file —
        // both streams managed by a single try-with-resources block.
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample-input.txt");
             OutputStream outputStream = Files.newOutputStream(tempOutputFile)) {
            String inputContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String outputContent = inputContent.replace("Input", "Output");

            outputStream.write(outputContent.getBytes(StandardCharsets.UTF_8));
        }

        String written = new String(Files.readAllBytes(tempOutputFile), StandardCharsets.UTF_8);
        assertThat(written).isEqualTo("Hello from Output File!\n");

        System.out.println("Output written to: " + tempOutputFile.toAbsolutePath());
    }

    @Test
    void after_try_with_resources_with_2_input_resources() throws IOException {
        // Write a derived file first so we have two distinct input sources to read simultaneously
        Path tempDerivedFile = Files.createTempFile("derived-", ".txt");
        try (InputStream source = getClass().getClassLoader().getResourceAsStream("sample-input.txt")) {
            String content = new String(source.readAllBytes(), StandardCharsets.UTF_8);
            Files.write(tempDerivedFile, content.replace("Input", "Derived").getBytes(StandardCharsets.UTF_8));
        }

        // Both resources opened and closed safely within a single try-with-resources
        try (InputStream original = getClass().getClassLoader().getResourceAsStream("sample-input.txt");
             InputStream derived = Files.newInputStream(tempDerivedFile)) {
            String originalContent = new String(original.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Original: " + originalContent.trim());

            String derivedContent = new String(derived.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Derived:  " + derivedContent.trim());

            assertThat(derivedContent).isEqualTo(originalContent.replace("Input", "Derived"));
        }

        System.out.println("Derived file created at: " + tempDerivedFile.toAbsolutePath());
    }

    @Test
    void file_operations_with_nio() throws IOException {
        Path tempFile1 = Files.createTempFile("nio-file-1-", ".txt");
        Path tempFile2 = Files.createTempFile("nio-file-2-", ".txt");

        // writes content using pre-Java-11 NIO (Files.write with byte array)
        Files.write(tempFile1, "Hello NIO.2".getBytes(StandardCharsets.UTF_8));

        // copies a file
        Files.copy(tempFile1, tempFile2, StandardCopyOption.REPLACE_EXISTING);

        // checks file attributes
        System.out.println("Temp file 1 created: " + tempFile1.toAbsolutePath());
        System.out.println("Temp file 2 (copy) created: " + tempFile2.toAbsolutePath());
        System.out.println("Is regular file: " + Files.isRegularFile(tempFile1));
        System.out.println("Is directory: " + Files.isDirectory(tempFile1));
        System.out.println("Size (byte): " + Files.size(tempFile2));

        assertThat(Files.isRegularFile(tempFile1)).isTrue();
        assertThat(Files.size(tempFile2)).isGreaterThan(0);
    }

    @Test
    void read_large_files_with_nio_channel() throws IOException {
        Path tempInputFile = Files.createTempFile("channel-read-", ".txt");
        Files.write(tempInputFile, "NIO Channel Buffer Read".getBytes(StandardCharsets.UTF_8));

        try (FileChannel channel = FileChannel.open(tempInputFile, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (channel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    char readChar = (char) buffer.get();
                    System.out.print(readChar);
                }
                buffer.clear();
            }
            System.out.println();
        }

        System.out.println("Channel read from temp file: " + tempInputFile.toAbsolutePath());
    }

    @Test
    void catch_multiple_exceptions_at_once() {
        try {
            Files.delete(Paths.get("/non-existent-directory/test-missing.txt"));
        } catch (NoSuchFileException | DirectoryNotEmptyException e) {
            System.out.println("No file, or not empty directory");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Any other exceptions");
            e.printStackTrace();
        }
    }

    @Test
    void read_and_write_numeric_constants_easier() {
        // before
        int mad = 1000000;

        // after
        int glad = 1_000_000;

        assertThat(glad).isEqualTo(mad);
    }

    @Test
    void switch_statements_by_string() {
        String answer = "Yes";
        String status;

        switch (answer) {
            case "Yes":
                status = "Accepted";
                break;
            case "No":
                status = "Declined";
                break;
            default:
                status = "Please answer";
        }

        assertThat(status).isEqualTo("Accepted");
    }

    @Test
    void divide_a_big_task_into_smaller_tasks_then_conquer_with_fork_join_framework() {
        // An array which contains numbers from 1 to 100
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        // Creates ForkJoin pool and task
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinArraySumTask task = new ForkJoinArraySumTask(10, array, 0, array.length);

        // Starts task
        int result = pool.invoke(task);

        assertThat(result).isEqualTo(5050);
    }

}
