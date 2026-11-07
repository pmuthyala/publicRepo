package org.example.marketdata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceSnapshotTest {

    @Test
    void shouldCreateValidSnapshot() {
        PriceSnapshot snapshot =
                new PriceSnapshot(15000, 15010, 1000, 101);

        assertEquals(15000, snapshot.bid());
        assertEquals(15010, snapshot.ask());
        assertEquals(1000, snapshot.timestamp());
        assertEquals(101, snapshot.sequence());
    }

    @Test
    void shouldRejectNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new PriceSnapshot(-100, 15010, 1000, 101));
    }

    @Test
    void shouldRejectBidGreaterThanAsk() {
        assertThrows(IllegalArgumentException.class,
                () -> new PriceSnapshot(15100, 15000, 1000, 101));
    }

    @Test
    void shouldRejectInvalidSequence() {
        assertThrows(IllegalArgumentException.class,
                () -> new PriceSnapshot(15000, 15010, 1000, 0));
    }

    @Test
    void shouldHaveValueBasedEquality() {
        PriceSnapshot first =
                new PriceSnapshot(15000, 15010, 1000, 101);

        PriceSnapshot second =
                new PriceSnapshot(15000, 15010, 1000, 101);

        assertEquals(first, second);
        assertNotSame(first, second);
    }
}

