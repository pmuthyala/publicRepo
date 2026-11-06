package org.example.marketdata;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Single-writer ownership eliminates competing writes to local state,
// while safe publication remains a separate concern.
public class SingleWriterCache {

    //// Accessed only by the designated writer thread.
    private final Map<String, PriceSnapshot> localState = new HashMap<>();

    // Published snapshots are visible to concurrent readers.
    private final ConcurrentHashMap<String, PriceSnapshot> published =
            new ConcurrentHashMap<>();

    public void update(String instrument, PriceSnapshot incoming) {
        PriceSnapshot current = localState.get(instrument);

        if (current == null ||
                incoming.sequence() > current.sequence()) {
            localState.put(instrument, incoming);
            published.put(instrument, incoming);
        }
    }

    public PriceSnapshot get(String instrument) {
        return published.get(instrument);
    }
}
