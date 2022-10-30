package io.github.ufuk.java09.examples;

public interface MyInterface {

    private static void doSomethingPrivatelyAndStatically() {
        System.out.println("Printing privately and statically");
    }

    private void doSomethingPrivately() {
        System.out.println("Printing privately");
    }

    default void doSomethingByDefaultUsingPrivateInterfaceMethod() {
        doSomethingPrivatelyAndStatically();
        doSomethingPrivately();
    }

}
