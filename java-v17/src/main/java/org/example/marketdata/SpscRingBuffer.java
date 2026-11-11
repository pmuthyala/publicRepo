package org.example.marketdata;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Objects;

public final class SpscRingBuffer<E> {

    private static final VarHandle PRODUCER_SEQUENCE;
    private static final VarHandle CONSUMER_SEQUENCE;

    static {
        try {
            CONSUMER_SEQUENCE = MethodHandles.lookup().findVarHandle(SpscRingBuffer.class, "consumerSequence", long.class);
            PRODUCER_SEQUENCE = MethodHandles.lookup().findVarHandle(SpscRingBuffer.class, "producerSequence", long.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    private final Object[] buffer;
    private final int mask;


    // Written only by producer; observed by consumer.
    private volatile long producerSequence;

    // Written only by consumer; observed by producer.
    private volatile long consumerSequence;

    public SpscRingBuffer(int capacity) {

        if(capacity < 2 || Integer.bitCount(capacity) != 1) {
            throw new IllegalArgumentException("capacity must be a power of two >= 2");
        }

        this.mask = capacity - 1;
        buffer = new Object[capacity];
    }

    public boolean offer(E event) {
        Objects.requireNonNull(event);

        long producer = this.producerSequence;
        long consumer = (long) CONSUMER_SEQUENCE.getAcquire(this);

        //buffer is full
        if((producer - consumer) >= buffer.length) {
            return false;
        }

        int index = (int) (producer & mask);
        buffer[index] = event;
        PRODUCER_SEQUENCE.setRelease(this, producer + 1);
        return true;
    }

    public E poll() {
        long consumer = this.consumerSequence;
        long producer = (long) PRODUCER_SEQUENCE.getAcquire(this);

        //Empty when producer and consumer are equal
        if(producer == consumer) {
            return null;
        }

        int index = (int) (consumer & mask);
        E event = (E) buffer[index];

        //Clear the consumed slot
        buffer[index] = null;
        CONSUMER_SEQUENCE.setRelease(this, consumer + 1);
        return event;
    }

    public int capacity() {
        return buffer.length;
    }
}
