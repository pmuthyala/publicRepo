package org.example.collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExample {

    static double fetchPrice(String exchange, double price) {
        System.out.println("Fetching from " + exchange +
                " on " + Thread.currentThread().getName());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return price;
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        IO.println("start at :: "+ start);

        //By default, CompletableFuture.supplyAsync() generally uses ForkJoinPool.commonPool().
        CompletableFuture<Double> nyse =
                CompletableFuture.supplyAsync(
                        () -> fetchPrice("NYSE", 185.50))
                            .completeOnTimeout(0.0, 2000, TimeUnit.MILLISECONDS);

        CompletableFuture<Double> nasdaq =
                CompletableFuture.supplyAsync(
                        () -> fetchPrice("NASDAQ", 186.20))
                        .completeOnTimeout(0.0, 1100, TimeUnit.MILLISECONDS);;

        CompletableFuture<Double> cboe =
                CompletableFuture.supplyAsync(
                        () -> fetchPrice("CBOE", 185.90))
                        .completeOnTimeout(0.0, 1100, TimeUnit.MILLISECONDS);;

        CompletableFuture<Void> all =
                CompletableFuture.allOf(nyse, nasdaq, cboe);

        CompletableFuture<Double> average = all.thenApply(v -> {
            return (nyse.join() + nasdaq.join() + cboe.join()) / 3;
        });

        average.thenAccept(price ->
                System.out.printf("Average price: %.2f%n", price)
        ).join();

        System.out.println("Elapsed: " +
                (System.currentTimeMillis() - start) + " ms");
    }
}
