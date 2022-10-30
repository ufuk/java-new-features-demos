package io.github.ufuk.java08.examples;

public interface MyInterface {

    static void doSomethingStatically() {
        System.out.println("Hello from interface's static method");
    }

    default void doSomethingEvenIfNotImplemented() {
        System.out.println("No need to implement");
    }

}
