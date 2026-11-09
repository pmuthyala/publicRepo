package org.example.marketdata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderedBookProcessorTest {

    @Test
    void shouldProcessUpdatesInSequenceOrder() {

        OrderedBookProcessor processor =
                new OrderedBookProcessor();

        assertTrue(processor.submit(
                new OrderedBookProcessor.Update(101, 15010)));

        assertTrue(processor.submit(
                new OrderedBookProcessor.Update(102, 15020)));

        assertTrue(processor.submit(
                new OrderedBookProcessor.Update(103, 15030)));

        assertTrue(processor.processNext());
        assertEquals(101, processor.lastSequence());
        assertEquals(15010, processor.bestBidTicks());

        assertTrue(processor.processNext());
        assertEquals(102, processor.lastSequence());
        assertEquals(15020, processor.bestBidTicks());

        assertTrue(processor.processNext());
        assertEquals(103, processor.lastSequence());
        assertEquals(15030, processor.bestBidTicks());

        assertFalse(processor.processNext());
    }

    @Test
    void shouldDetectSequenceGap() {
        OrderedBookProcessor processor =
                new OrderedBookProcessor();

        processor.submit(new OrderedBookProcessor.Update(101, 15010));
        processor.submit(new OrderedBookProcessor.Update(102, 15020));
        processor.submit(new OrderedBookProcessor.Update(104, 15040));

        assertTrue(processor.processNext());
        assertTrue(processor.processNext());

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        processor::processNext
                );

        assertTrue(exception.getMessage().contains("103"));

        // Invalid update must not change book state.
        assertEquals(102, processor.lastSequence());
        assertEquals(15020, processor.bestBidTicks());
    }

    @Test
    void shouldRejectUpdatesWhenQueueIsFull() {
        OrderedBookProcessor processor =
                new OrderedBookProcessor();

        // Fill the queue to its capacity of 10,000.
        for (int i = 1; i <= 10000; i++) {
            assertTrue(processor.submit(
                    new OrderedBookProcessor.Update(i, 15000 + i)
            ));
        }

        // The next update must be rejected.
        assertFalse(processor.submit(
                new OrderedBookProcessor.Update(10001, 25001)
        ));

        // Processing one update frees a queue slot.
        assertTrue(processor.processNext());

        // Submission should now succeed.
        assertTrue(processor.submit(
                new OrderedBookProcessor.Update(10001, 25001)
        ));
    }
}
