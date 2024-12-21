package io.github.ufuk.java09.examples;

import java.util.concurrent.Flow;

public class IntegerSubscriber implements Flow.Subscriber<Integer> {

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        System.out.println("Subscribed! Thread name: " + Thread.currentThread().getName());
        subscription.request(1); // Request first item
    }

    @Override
    public void onNext(Integer item) {
        System.out.println("Item '" + item + "' received by " + Thread.currentThread().getName());
        // Do something
        subscription.request(1); // Request next item
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error occurred: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("All items received by " + Thread.currentThread().getName());
    }

}
