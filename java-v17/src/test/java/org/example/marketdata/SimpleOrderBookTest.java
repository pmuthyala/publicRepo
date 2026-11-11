
package org.example.marketdata;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class SimpleOrderBookTest {

    private static SimpleOrderBook.Snapshot awaitSequence(
            SimpleOrderBook book, long sequence) throws Exception {

        // Give the worker up to 3 seconds.
        long deadline = System.nanoTime() +
                TimeUnit.SECONDS.toNanos(3);
        System.out.println("deadline: " + deadline);

        while (System.nanoTime() < deadline) {

            // Fail immediately if the worker encountered an error.
            if (book.failure() != null) {
                fail("Worker failed: " + book.failure());
            }

            // Read the latest published immutable snapshot.
            var snapshot = book.snapshot();

            // Has the worker processed the expected sequence?
            if (snapshot.sequence() >= sequence) {
                return snapshot;
            }

            // Wait briefly before checking again.
            Thread.sleep(1);
        }

        fail("Timed out waiting for sequence " + sequence);
        return null;
    }

    //@Test
    void shouldAddAndAggregatePriceLevels() throws Exception {
        try (SimpleOrderBook book = new SimpleOrderBook(100)) {
            assertTrue(book.submit(new SimpleOrderBook.Event(
                    1, SimpleOrderBook.Type.ADD,
                    101, SimpleOrderBook.Side.BUY, 15000, 100)));

            assertTrue(book.submit(new SimpleOrderBook.Event(
                    2, SimpleOrderBook.Type.ADD,
                    102, SimpleOrderBook.Side.BUY, 15000, 50)));

            assertTrue(book.submit(new SimpleOrderBook.Event(
                    3, SimpleOrderBook.Type.ADD,
                    103, SimpleOrderBook.Side.SELL, 15010, 75)));

            var snapshot = awaitSequence(book, 3);

            assertEquals(150, snapshot.bids().get(0).quantity());
            assertEquals(15000, snapshot.bids().get(0).priceTicks());
            assertEquals(75, snapshot.asks().get(0).quantity());
        }
    }

    @Test
    void shouldModifyAndCancelOrders() throws Exception {
        try (SimpleOrderBook book = new SimpleOrderBook(100)) {
            book.submit(new SimpleOrderBook.Event(
                    1, SimpleOrderBook.Type.ADD,
                    101, SimpleOrderBook.Side.BUY, 15000, 100));

            book.submit(new SimpleOrderBook.Event(
                    2, SimpleOrderBook.Type.MODIFY,
                    101, SimpleOrderBook.Side.BUY, 15005, 60));

            var modified = awaitSequence(book, 2);
            assertEquals(15005, modified.bids().get(0).priceTicks());
            assertEquals(60, modified.bids().get(0).quantity());

            book.submit(new SimpleOrderBook.Event(
                    3, SimpleOrderBook.Type.CANCEL,
                    101, null, 0, 0));

            var canceled = awaitSequence(book, 3);
            assertTrue(canceled.bids().isEmpty());
        }
    }

    //@Test
    void shouldDetectSequenceGap() throws Exception {
        try (SimpleOrderBook book = new SimpleOrderBook(100)) {
            book.submit(new SimpleOrderBook.Event(
                    1, SimpleOrderBook.Type.ADD,
                    101, SimpleOrderBook.Side.BUY, 15000, 100));

            awaitSequence(book, 1);

            book.submit(new SimpleOrderBook.Event(
                    3, SimpleOrderBook.Type.ADD,
                    102, SimpleOrderBook.Side.BUY, 15001, 50));

            long deadline = System.nanoTime() +
                    TimeUnit.SECONDS.toNanos(3);

            while (book.failure() == null &&
                    System.nanoTime() < deadline) {
                Thread.sleep(1);
            }

            assertNotNull(book.failure());
            assertInstanceOf(IllegalStateException.class, book.failure());
            assertEquals(1, book.snapshot().sequence());
        }
    }

    //@Test
    void shouldAllowConcurrentSnapshotReaders() throws Exception {
        try (SimpleOrderBook book = new SimpleOrderBook(1000)) {
            ExecutorService readers = Executors.newFixedThreadPool(4);

            try {
                CountDownLatch start = new CountDownLatch(1);

                Future<?>[] futures = new Future<?>[4];

                for (int i = 0; i < 4; i++) {
                    futures[i] = readers.submit(() -> {
                        start.await();

                        for (int j = 0; j < 1000; j++) {
                            var snapshot = book.snapshot();

                            assertNotNull(snapshot.bids());
                            assertNotNull(snapshot.asks());
                        }
                        return null;
                    });
                }

                start.countDown();

                for (int i = 1; i <= 100; i++) {
                    assertTrue(book.submit(new SimpleOrderBook.Event(
                            i, SimpleOrderBook.Type.ADD,
                            i, SimpleOrderBook.Side.BUY,
                            15000 + i, 10)));
                }

                awaitSequence(book, 100);

                for (Future<?> future : futures) {
                    future.get(3, TimeUnit.SECONDS);
                }
            } finally {
                readers.shutdownNow();
            }
        }
    }
}
