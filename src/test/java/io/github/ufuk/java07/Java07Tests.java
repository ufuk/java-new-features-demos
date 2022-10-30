package io.github.ufuk.java07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Further readings:
 * - https://howtodoinjava.com/java7/java-7-changes-features-and-enhancements
 */
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
            inputStream = getClass().getClassLoader().getResourceAsStream("test1.txt");

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
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test1.txt")) {
            String fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void after_try_with_resources_with_1_input_1_output_resource() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test1.txt");
             OutputStream outputStream = new FileOutputStream("src/test/resources/test2.txt")) {
            String fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println(fileContent);

            outputStream.write(fileContent.replace('1', '2').getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void after_try_with_resources_with_2_input_resources() {
        try (InputStream inputStream1 = getClass().getClassLoader().getResourceAsStream("test1.txt");
             InputStream inputStream2 = getClass().getClassLoader().getResourceAsStream("test2.txt")) {
            String fileContent1 = new String(inputStream1.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(fileContent1);

            String fileContent2 = new String(inputStream2.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(fileContent2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void file_operations_with_nio() throws IOException {
        Path pathTestFile3 = Paths.get("src/test/resources/test3.txt");
        Path pathTestFile4 = Paths.get("src/test/resources/test4.txt");

        // deletes files
        Files.deleteIfExists(pathTestFile3);
        Files.deleteIfExists(pathTestFile4);

        // creates new file
        Files.createFile(pathTestFile3);

        // creates new temp file
        Path tempFile = Files.createTempFile("prefix", "suffix");

        // writes absolute path
        System.out.println(pathTestFile3.toAbsolutePath());

        // copies a file
        Files.copy(Paths.get("src/test/resources/test2.txt"), pathTestFile4);

        // checks file attributes
        System.out.println("Is regular file: " + Files.isRegularFile(pathTestFile3));
        System.out.println("Is directory file: " + Files.isDirectory(pathTestFile3));
        System.out.println("Size (byte): " + Files.size(pathTestFile4));
        System.out.println("Is regular file: " + Files.isRegularFile(Paths.get("src/test/resources/")));
        System.out.println("Is directory file: " + Files.isDirectory(Paths.get("src/test/resources/")));

        // deletes files
        Files.delete(pathTestFile3);
        Files.delete(pathTestFile4);
    }

    @Test
    void read_large_files_with_nio_channel() {
        try (RandomAccessFile aFile = new RandomAccessFile("src/test/resources/test1.txt", "r");
             FileChannel channel = aFile.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (channel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    char readChar = (char) buffer.get();

                    System.out.print(readChar);
                }
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void catch_multiple_exceptions_at_once() {
        try {
            Files.delete(Paths.get("src/test/resources/test5.txt"));
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
    }

    @Test
    void switch_statements_by_string() {
        String answer = "Yes";

        switch (answer) {
            case "Yes":
                System.out.println("Accepted");
                break;
            case "No":
                System.out.println("Declined");
                break;
            default:
                System.out.println("Please answer");
        }
    }

    @Test
    void fork_join_framework() {
        Assertions.fail("No example presents"); // TODO: add example(s)
    }

}
