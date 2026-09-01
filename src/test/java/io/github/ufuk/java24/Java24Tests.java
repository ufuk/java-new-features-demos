package io.github.ufuk.java24;

import org.junit.jupiter.api.Test;

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 24 Release Notes](https://www.oracle.com/java/technologies/javase/24all-relnotes.html)
/// - [Java 24 Features (with Examples)](https://www.happycoders.eu/java/java-24-features/)
/// - [Java 24 Release Notes Review for Developers - Inside Java Newscast #87](https://www.youtube.com/watch?v=CV7VAWRlEW0)
class Java24Tests {

    private final Object sharedLock = new Object();

    @Test
    void virtual_threads_no_longer_pin_carrier_threads_inside_synchronized_blocks() throws InterruptedException { // released in Java 24
        // In Java 21 to 23, blocking I/O or sleep inside `synchronized` blocks caused Virtual Threads
        // to "pin" the underlying platform (carrier) thread.
        // JEP 491 in Java 24 completely resolves this by making `synchronized` fully cooperative with Virtual Threads.
        List<String> completedTasks = Collections.synchronizedList(new ArrayList<>());

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 1; i <= 5; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    synchronized (sharedLock) {
                        // Virtual thread can yield and unmount cleanly inside synchronized blocks in Java 24
                        Thread.sleep(Duration.ofMillis(50));
                        completedTasks.add("Task-" + taskId);
                    }
                    return null;
                });
            }
        } // Wait for all virtual threads to complete

        assertThat(completedTasks).hasSize(5);
    }

    @Test
    void class_file_api_for_parsing_and_inspecting_bytecode() throws Exception { // preview in Java 22, released in Java 24
        // JEP 484 introduces a standard API for reading, transforming, and generating Java bytecode
        // without needing external libraries like ASM or ByteBuddy.
        try (var is = Java24Tests.class.getResourceAsStream("Java24Tests.class")) {
            if (is != null) {
                byte[] classBytes = is.readAllBytes();
                ClassModel classModel = ClassFile.of().parse(classBytes);

                List<String> methodNames = classModel.methods().stream()
                        .map(MethodModel::methodName)
                        .map(utf8Entry -> utf8Entry.stringValue())
                        .toList();

                assertThat(classModel.thisClass().asInternalName()).contains("Java24Tests");
                assertThat(methodNames).contains(
                        "<init>",
                        "virtual_threads_no_longer_pin_carrier_threads_inside_synchronized_blocks",
                        "class_file_api_for_parsing_and_inspecting_bytecode"
                );
            }
        }
    }

    @Test
    void compact_object_headers() { // experimental in Java 24, released in Java 25
        /*
        JEP 450 (Compact Object Headers) reduces the size of Java object headers from
        96–128 bits (12–16 bytes) down to 64 bits (8 bytes) on 64-bit architectures.

        Key Developer Benefits:
        - Reduces overall heap memory footprint by 10% to 20% on real-world applications
        - Increases CPU L1/L2 cache locality, leading to higher cache hit rates and throughput
        - Enabled via `-XX:+UnlockExperimentalVMOptions -XX:+UseCompactObjectHeaders`
        */
    }

}
