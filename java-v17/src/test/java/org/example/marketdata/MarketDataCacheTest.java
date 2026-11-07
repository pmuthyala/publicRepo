package org.example.marketdata;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class MarketDataCacheTest {

    @Test
    void shouldInsertNewInstrument() {
        MarketDataCache cache = new MarketDataCache();

        PriceSnapshot price =
                new PriceSnapshot(15000, 15010, 1000, 101);

        cache.update("AAPL", price);

        assertEquals(price, cache.get("AAPL"));
        assertEquals(1, cache.size());
    }

    @Test
    void shouldReplaceOlderSequence() {
        MarketDataCache cache = new MarketDataCache();

        cache.update("AAPL",
                new PriceSnapshot(15000, 15010, 1000, 100));

        cache.update("AAPL",
                new PriceSnapshot(15100, 15110, 1001, 101));

        assertEquals(101, cache.get("AAPL").sequence());
        assertEquals(15100, cache.get("AAPL").bid());
    }

    @Test
    void shouldRejectStaleUpdate() {
        MarketDataCache cache = new MarketDataCache();

        cache.update("AAPL",
                new PriceSnapshot(15100, 15110, 1001, 101));

        cache.update("AAPL",
                new PriceSnapshot(15000, 15010, 1000, 100));

        assertEquals(101, cache.get("AAPL").sequence());
    }

    @Test
    void shouldHandleConcurrentWriters() throws Exception {
        MarketDataCache cache = new MarketDataCache();

        int threadCount = 8;
        int updatesPerThread = 1000;

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();

        try {
            for (int t = 0; t < threadCount; t++) {
                final int threadId = t;

                futures.add(executor.submit(() -> {
                    start.await();

                    for (int i = 1; i <= updatesPerThread; i++) {
                        long sequence =
                                threadId * updatesPerThread + i;

                        cache.update("AAPL",
                                new PriceSnapshot(
                                        15000, 15010,
                                        sequence, sequence));
                    }
                    return null;
                }));
            }

            start.countDown();

            for (Future<?> future : futures) {
                future.get(10, TimeUnit.SECONDS);
            }

            assertEquals(
                    threadCount * updatesPerThread,
                    cache.get("AAPL").sequence());

            assertEquals(1, cache.size());

        } finally {
            executor.shutdownNow();
        }
    }
}
