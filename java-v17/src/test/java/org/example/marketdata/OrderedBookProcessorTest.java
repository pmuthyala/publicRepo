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
}
