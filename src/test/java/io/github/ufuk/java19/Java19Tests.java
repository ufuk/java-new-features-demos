package io.github.ufuk.java19;

import io.github.ufuk.java17.examples.Cat;
import io.github.ufuk.java17.examples.Dog;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

/// Further readings:
/// - [JDK 19 Release Notes](https://www.oracle.com/java/technologies/javase/19all-relnotes.html)
/// - [Record Patterns in Java 19](https://www.baeldung.com/java-19-record-patterns)
/// - [JDK 19: The New Features in Java 19](https://www.infoworld.com/article/3653331/jdk-19-the-new-features-in-java-19.html)
/// - [Java 19 Features (with Examples)](https://www.happycoders.eu/java/java-19-features/)
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
        assertThat(name).isEqualTo("Sıdıka*");
    }

    @Test
    void using_instanceof_and_record_paterns() { // preview in Java 19, released in Java 21
        Object animal = new Cat("Sıdıka");
        String extractedName = null;

        if (animal instanceof Cat(String name)) {
            System.out.println(name); // instead of this -> System.out.println(((Cat) animal).name());
            extractedName = name;
        }

        assertThat(extractedName).isEqualTo("Sıdıka");
    }

    @Test
    void using_switch_and_record_paterns() { // preview in Java 19, released in Java 21
        Object animal = new Cat("Sıdıka");

        String description = switch (animal) {
            case Cat(String name) -> name + " the cat";
            case Dog(String name) -> name + " the dog";
            default -> "unidentified animal";
        };

        System.out.println(description);
        assertThat(description).isEqualTo("Sıdıka the cat");
    }

    @Test
    void use_virtual_threads_without_blocking_platform_thread() throws InterruptedException { // preview in Java 19, released in Java 21
        // option 1
        Thread thread1 = Thread.startVirtualThread(() -> {
            System.out.println("Hello from Virtual Thread 1");
        });
        thread1.join();
        assertThat(thread1.isVirtual()).isTrue();

        // option 2
        Thread thread2 = Thread.ofVirtual()
                .start(() -> {
                    System.out.println("Hello from Virtual Thread 2");
                });
        thread2.join();
        assertThat(thread2.isVirtual()).isTrue();

        // new util method to create old-school platform threads
        Thread platformThread = Thread.ofPlatform()
                .start(() -> {
                    System.out.println("Hello from Platform Thread");
                });
        platformThread.join();
        assertThat(platformThread.isVirtual()).isFalse();

        System.out.println("Virtual threads are great!");
    }

    @Test
    void create_new_virtual_thread_per_task_instead_of_creating_a_pool_of_old_school_threads() throws InterruptedException { // preview in Java 19, released in Java 21
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        var threadCount = 250;
        Set<Long> setOfThreadIds = ConcurrentHashMap.newKeySet();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                System.out.println("Am I a virtual thread? -> " + Thread.currentThread().isVirtual() + " " + Thread.currentThread().threadId());
                setOfThreadIds.add(Thread.currentThread().threadId());
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertThat(setOfThreadIds).hasSize(threadCount);
    }

    @Test
    void future_and_completable_future_state_and_immediate_result_inspection() {
        // Java 19 added state(), resultNow(), and exceptionNow() to the Future interface.
        // This eliminates checked exception try-catch boilerplate when we already know the future has completed.
        Future<String> successfulFuture = CompletableFuture.completedFuture("Hello from Future!");

        // before: had to handle checked InterruptedException / ExecutionException with try-catch
        // try { String val = successfulFuture.get(); } catch (Exception e) { ... }

        // after: if state is SUCCESS, resultNow() returns the value immediately without checked exceptions
        if (successfulFuture.state() == Future.State.SUCCESS) {
            String result = successfulFuture.resultNow();
            System.out.println("Result: " + result);
            assertThat(result).isEqualTo("Hello from Future!");
        }

        // if state is FAILED, exceptionNow() returns the underlying Throwable
        Future<String> failedFuture = CompletableFuture.failedFuture(new IllegalStateException("Service Error"));
        if (failedFuture.state() == Future.State.FAILED) {
            Throwable exception = failedFuture.exceptionNow();
            System.out.println("Exception: " + exception.getMessage());
            assertThat(exception).isInstanceOf(IllegalStateException.class);
        }
    }

}
