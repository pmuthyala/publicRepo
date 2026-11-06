package org.example.concurrency;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class VarHandleCounter {

    private volatile int counter = 0;
    private int unsafeCounter = 0;

    private static final VarHandle COUNTER;

    static {
        try {
            COUNTER = MethodHandles.lookup()
                    .findVarHandle(
                            VarHandleCounter.class,
                            "counter",
                            int.class
                    );
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void incrementAtomic() {
        int current;
        do {
            current = (int) COUNTER.getVolatile(this);
        } while (!COUNTER.compareAndSet(
                this, current, current + 1
        ));
    }

    public void incrementUnsafe() {
        unsafeCounter++;
    }

    public int getCounter() {
        return (int) COUNTER.getVolatile(this);
    }

    public int getUnsafeCounter() {
        return unsafeCounter;
    }
}

