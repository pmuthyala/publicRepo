package org.example.concurrency;

public class SpinWaitExample {
    private static volatile boolean ready;

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            while (!ready) {
                Thread.onSpinWait();
            }
            System.out.println("Ready observed");
        });
        worker.start();
        Thread.sleep(100);
        ready = true;
        worker.join();
    }
}