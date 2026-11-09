package org.example.marketdata;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class OrderedBookProcessor {
    public record Update(long sequence, long bidPriceTicks) {}

    private final BlockingQueue<Update> queue = new ArrayBlockingQueue<>(10000);

    private long lastSequence = 0;
    private long bestBidTicks = 0;

    public boolean submit(Update update) {
        return queue.offer(update);
    }

    public void runWorker() throws InterruptedException {
        while(!Thread.currentThread().isInterrupted()) {
            Update update = queue.take();

            if(lastSequence !=0 &&  update.sequence() != (lastSequence + 1)) {
                throw new IllegalStateException("Sequence gap: expected "
                            + (lastSequence + 1) + " got " + update.sequence());
            }

            bestBidTicks = update.bidPriceTicks();
            lastSequence = update.sequence();
        }
    }

    public boolean processNext() {
        Update update = queue.poll();

        if(update == null) {
            return false;
        }

        if(lastSequence != 0 && update.sequence() != (lastSequence + 1)) {
            throw new IllegalStateException("Sequence gap: expected "
                    + (lastSequence + 1) + " got " + update.sequence());
        }
        lastSequence = update.sequence();
        bestBidTicks = update.bidPriceTicks();
        return true;
    }

    public long lastSequence() {
        return lastSequence;
    }

    public long bestBidTicks() {
        return bestBidTicks;
    }
}
