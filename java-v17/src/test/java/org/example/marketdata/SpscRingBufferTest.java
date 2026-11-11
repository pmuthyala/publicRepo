package org.example.marketdata;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class SpscRingBufferTest {

    @Test
    void shouldPreserveFifoOrder() {
        var queue = new SpscRingBuffer<Integer>(8);

        assertTrue(queue.offer(10));
        assertTrue(queue.offer(20));
        assertTrue(queue.offer(30));

        assertEquals(10, queue.poll());
        assertEquals(20, queue.poll());
        assertEquals(30, queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void shouldHandleFullAndEmpty() {
        var queue = new SpscRingBuffer<Integer>(2);

        assertNull(queue.poll());

        assertTrue(queue.offer(1));
        assertTrue(queue.offer(2));
        assertFalse(queue.offer(3));

        assertEquals(1, queue.poll());
        assertTrue(queue.offer(3));

        assertEquals(2, queue.poll());
        assertEquals(3, queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void shouldTransferEventsBetweenThreads() throws Exception {
        final int count = 100000;
        var queue = new SpscRingBuffer<Integer>(1024);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Future<?> producer = executor.submit(() -> {
                start.await();

                for (int i = 0; i < count; i++) {
                    while (!queue.offer(i)) {
                        Thread.onSpinWait();
                    }
                }

                return null;
            });

            Future<?> consumer = executor.submit(() -> {
                start.await();

                for (int expected = 0; expected < count; expected++) {
                    Integer actual;

                    while ((actual = queue.poll()) == null) {
                        Thread.onSpinWait();
                    }

                    assertEquals(expected, actual.intValue());
                }

                return null;
            });

            start.countDown();

            producer.get(1, TimeUnit.SECONDS);
            consumer.get(1, TimeUnit.SECONDS);

        } finally {
            executor.shutdownNow();
        }
    }
}