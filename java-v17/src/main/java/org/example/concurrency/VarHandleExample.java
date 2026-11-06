package org.example.concurrency;

import java.lang.invoke.VarHandle;
import java.lang.invoke.MethodHandles;

public class VarHandleExample {
    private volatile int value;

    // VarHandle is a typed, reflection-like mechanism introduced in Java 9
    // that provides low-level variable access with atomic operations and configurable
    // memory-ordering semantics, without relying on sun.misc.Unsafe.
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(VarHandleExample.class, "value", int.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void increment() {
        int current;
        do {
            current = (int) VALUE.getVolatile(this);
        } while (!VALUE.compareAndSet(
                this,
                current,
                current + 1
        ));
    }
}

