
package org.example.concurrency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarHandleTest {

    @Test
    void shouldInitializeCounterToZero() {
        VarHandleCounter counter = new VarHandleCounter();

        assertEquals(0, counter.getCounter());
    }

    @Test
    void shouldIncrementAtomically() {
        VarHandleCounter counter = new VarHandleCounter();

        counter.incrementAtomic();
        counter.incrementAtomic();

        assertEquals(2, counter.getCounter());
    }

    @Test
    void shouldIncrementCorrectlyWithTwoThreads()
            throws InterruptedException {

        VarHandleCounter counter = new VarHandleCounter();

        int iterations = 100_000;

        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                counter.incrementAtomic();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertEquals(
                iterations * 2,
                counter.getCounter()
        );
    }

    @Test
    void shouldMaintainIndependentCounters() {
        VarHandleCounter first = new VarHandleCounter();
        VarHandleCounter second = new VarHandleCounter();

        first.incrementAtomic();
        first.incrementAtomic();
        second.incrementAtomic();

        assertEquals(2, first.getCounter());
        assertEquals(1, second.getCounter());
    }
}
