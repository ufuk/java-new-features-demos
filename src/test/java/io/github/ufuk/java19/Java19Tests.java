package io.github.ufuk.java19;

import io.github.ufuk.java17.examples.Cat;
import io.github.ufuk.java17.examples.Dog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Further readings:
 * - https://www.oracle.com/java/technologies/javase/19-relnote-issues.html
 * - https://www.baeldung.com/java-19-record-patterns
 * - https://www.infoworld.com/article/3653331/jdk-19-the-new-features-in-java-19.html
 * - https://www.happycoders.eu/java/java-19-features/
 */
class Java19Tests {

    @Test
    void condition_with_the_when_keyword_using_pattern_matching_for_switch() { // preview in Java 17, still preview in Java 19, released in Java 21
        Object animal = new Cat("Sıdıka");

        String name = switch (animal) {
            case Cat c when c.name().endsWith("a") -> c.name() + "*";
            case Cat c -> c.name();
            case Dog d -> d.name();
            default -> "unidentified animal";
        };

        System.out.println(name);
    }

    @Test
    void using_instanceof_and_record_paterns() { // preview in 19, released in Java 21
        Object animal = new Cat("Sıdıka");

        if (animal instanceof Cat(String name)) {
            System.out.println(name); // instead of this -> System.out.println(((Cat) animal).name());
        }
    }

    @Test
    void using_switch_and_record_paterns() { // preview in 19, released in Java 21
        Object animal = new Cat("Sıdıka");

        switch (animal) {
            case Cat(String name) -> System.out.println(name + " the cat");
            case Dog(String name) -> System.out.println(name + " the dog");
            default -> System.out.println("unidentified animal");
        }
    }

    @Test
    void use_virtual_threads_without_blocking_platform_thread() throws InterruptedException { // preview in 19, released in Java 21
        // option 1
        Thread.startVirtualThread(() -> {
                    System.out.println("Hello from Virtual Thread 1");
                })
                .join();

        // option 1
        Thread.ofVirtual()
                .start(() -> {
                    System.out.println("Hello from Virtual Thread 2");
                })
                .join();

        // new util method to create old-school platform threads
        Thread.ofPlatform()
                .start(() -> {
                    System.out.println("Hello from Platform Thread");
                })
                .join();

        System.out.println("Virtual threads are great!");
    }

    @Test
    void create_new_virtual_thread_per_task_instead_of_creating_a_pool_of_old_school_threads() throws InterruptedException { // preview in 19, released in Java 21
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println("Am I a virtual thread? -> " + Thread.currentThread().isVirtual());
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    void foreign_function_and_memory_api() { // preview in 19, released in Java ?
        Assertions.fail("No example presents"); // TODO: add example(s)
    }

}
