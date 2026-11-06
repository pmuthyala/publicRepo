package org.example.marketdata;

import java.util.concurrent.ConcurrentHashMap;

public class MarketDataCache {

    //ConcurrentHashMap provides thread-safe map operations;
    //compute() lets us enforce an atomic business rule during replacement.
    private final ConcurrentHashMap<String, PriceSnapshot> prices =
            new ConcurrentHashMap<>();

    public void update(String instrument, PriceSnapshot incoming) {
        //put() always replaces the current value.
        //compute() allows you to compare the existing value and the incoming value atomically for that key.
        prices.compute(instrument, (key, current) -> {
            if (current == null ||
                    incoming.sequence() > current.sequence()) {
                return incoming;
            }
            return current;
        });
    }

    public PriceSnapshot get(String instrument) {
        return prices.get(instrument);
    }

    public int size() {
        return prices.size();
    }
}
