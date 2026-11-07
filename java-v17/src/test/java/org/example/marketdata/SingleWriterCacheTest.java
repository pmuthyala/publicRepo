
package org.example.marketdata;
import org.junit.jupiter.api.Test;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class SingleWriterCacheTest {

    @Test
    void shouldRejectStaleUpdate() {
        SingleWriterCache cache = new SingleWriterCache();

        cache.update("AAPL",
                new PriceSnapshot(15100, 15110, 1001, 101));

        cache.update("AAPL",
                new PriceSnapshot(15000, 15010, 1000, 100));

        assertEquals(101, cache.get("AAPL").sequence());
    }

    @Test
    void shouldPublishSnapshotsToReaderThread()
            throws Exception {

        SingleWriterCache cache = new SingleWriterCache();

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        try {
            // Main test thread is the only writer.
            cache.update("AAPL",
                    new PriceSnapshot(15000, 15010, 1000, 101));

            Future<PriceSnapshot> reader =
                    executor.submit(() -> cache.get("AAPL"));

            PriceSnapshot snapshot =
                    reader.get(5, TimeUnit.SECONDS);

            assertNotNull(snapshot);
            assertEquals(101, snapshot.sequence());
            assertEquals(15000, snapshot.bid());

        } finally {
            executor.shutdownNow();
        }
    }
}
