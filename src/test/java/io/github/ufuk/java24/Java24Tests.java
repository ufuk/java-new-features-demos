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
    void virtual_threads_no_longer_pin_carrier_threads_inside_synchronized_blocks() throws Exception { // released in Java 24
        /*
        Historical Problem (Java 21 to 23):
        ------------------------------------
        When a virtual thread blocked on I/O or sleep inside a `synchronized` block/method,
        it "pinned" its underlying carrier (platform) thread in the ForkJoinPool.
        Because the carrier thread could not be unmounted:
        1. Carrier pool threads were easily exhausted by a few blocking synchronized calls.
        2. Other virtual threads were starved and unable to execute.
        3. Developers had to refactor synchronized code to `java.util.concurrent.locks.ReentrantLock`.

        Solution in Java 24 (JEP 491):
        -------------------------------
        The JVM's monitor implementation was re-engineered so virtual threads unmount cleanly
        even when blocking inside `synchronized` blocks or methods. Legacy synchronized code
        and third-party libraries now scale effortlessly on virtual threads without pinning.
        */

        int taskCount = 10;
        List<String> results = Collections.synchronizedList(new ArrayList<>());

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 1; i <= taskCount; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    // In Java 21-23, entering `synchronized` and sleeping would pin the carrier thread.
                    // In Java 24, the virtual thread unmounts cleanly during sleep, allowing carrier threads to run other tasks.
                    synchronized (sharedLock) {
                        System.out.println("Task " + taskId + " running inside synchronized on " + Thread.currentThread());
                        Thread.sleep(Duration.ofMillis(30));
                        results.add("Task-" + taskId);
                    }
                    return null;
                });
            }
        } // Awaits completion of all virtual threads

        assertThat(results).hasSize(taskCount);
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
