package org.example.marketdata;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class SimpleOrderBook implements AutoCloseable {

    public enum Side {BUY, SELL}
    public enum Type {ADD, MODIFY, CANCEL}

    public record Event(
            long sequence,
            Type type,
            long orderId,
            Side side,
            long priceTicks,
            long quantity
    ) {
    }

    public record Level(
            long priceTicks,
            long quantity
    ) {
    }

    public record Snapshot(
        long sequence,
        List<Level> bids,
        List<Level> asks
    ) {}

    private record Order(
            Side side,
            long priceTicks,
            long quantity
    ) {
    }

    private final BlockingQueue<Event> queue;
    private final Thread worker;

    //Mutable State Owned Only by Worker Thread
    private final Map<Long, Order> orders = new HashMap<>();
    private final NavigableMap<Long, Long> bids = new TreeMap<>(Comparator.reverseOrder());
    private final NavigableMap<Long, Long> asks = new TreeMap<>();

    private long lastSequence = 0;

    //Immutable State safely published to readers
    private volatile Snapshot latest = new Snapshot(0, List.of(), List.of());

    private volatile boolean running = true;
    private volatile Throwable failure;

    public SimpleOrderBook(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
        worker = new Thread(this::run, "order-book-worker");
        worker.start();
    }

    public boolean submit(Event event) {
            if(!running || failure != null) {
                return false;
            }
            return queue.offer(event);
    }

    public Snapshot snapshot() {
        return latest;
    }

    public Throwable failure() {
        return failure;
    }

    private void run() {
        try {
            while(running || !queue.isEmpty()){
                Event event = queue.poll(100, TimeUnit.MILLISECONDS);
                if(event == null) continue;

                apply(event);
                publishSnapshot();
            }
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Throwable t) {
            failure = t;
            running = false;
        }
    }

    private void apply(Event event) {
        if (lastSequence != 0 &&
                event.sequence() != (lastSequence + 1)) {
            throw new IllegalStateException("Sequence gap: expected "
                    + (lastSequence + 1) + " got " + event.sequence());
        }

        switch (event.type()) {
            case ADD -> add(event);
            case MODIFY -> modify(event);
            case CANCEL -> cancel(event);
        }

        lastSequence = event.sequence();
    }

    private void add(Event e) {
        if(orders.containsKey(e.orderId())) {
            throw new IllegalStateException("Duplicate order id: " + e.orderId());
        }
        if(e.quantity() <= 0 || e.priceTicks() <= 0) {
            throw new IllegalArgumentException("Invalid Order ");
        }
        orders.put(e.orderId(), new Order(e.side(), e.priceTicks(), e.quantity()));
        changeLevel(e.side(), e.priceTicks(), e.quantity());
    }

    private void modify(Event e) {
        Order old = orders.get(e.orderId());
        if(old==null) {
            throw new IllegalStateException("Unknown order id: " + e.orderId());
        }
        Side side = e.side() !=null ? e.side() : old.side();
        if(e.priceTicks() <=0 || e.quantity() <= 0) {
            throw new IllegalArgumentException("Invalid Order Modification ");
        }

        changeLevel(old.side(), old.priceTicks(), -old.quantity());

        orders.put(e.orderId(), new Order(side, e.priceTicks(), e.quantity()));

        changeLevel(side, e.priceTicks(), e.quantity());
    }

    private void cancel(Event e) {
        Order old = orders.remove(e.orderId());

        if(old==null) {
            throw new IllegalStateException("Unknown order id: " + e.orderId());
        }

        changeLevel(old.side(), old.priceTicks(), -old.quantity());
    }

    private void changeLevel(Side side, long price, long delta) {
        NavigableMap<Long, Long> levels = (side == Side.BUY) ? bids : asks;

        System.out.println("changeLevel: " + side + " " + price + " " + delta);
        long updated = levels.getOrDefault(price, 0L) + delta;

        if(updated < 0) {
            throw new IllegalStateException("Negative quantity at " + price);
        }

        if(updated == 0) {
            levels.remove(price);
        } else {
            levels.put(price, updated);
        }
        System.out.println("changeLevel values : " + levels);
    }

    private void publishSnapshot() {
        latest = new Snapshot(
            lastSequence,
            List.copyOf(bids.entrySet().stream().
                    map(e-> new Level(e.getKey(), e.getValue())).toList()),
            List.copyOf(asks.entrySet().stream().
                    map(e-> new Level(e.getKey(), e.getValue())).toList())
            );
        System.out.println("publishSnapshot: " + latest);
    }

    @Override
    public void close() throws InterruptedException {
        running = false;
        worker.join(5000);
        if(worker.isAlive()) {
            worker.interrupt();
            worker.join();
        }
    }
}
