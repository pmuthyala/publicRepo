package org.example.marketdata;
public class MarketDataDemo {

    public static void main(String[] args) {

        PriceSnapshot newer =
                new PriceSnapshot(15000, 15010, 1000, 101);

        PriceSnapshot older =
                new PriceSnapshot(14900, 14910, 999, 100);

        // ConcurrentHashMap implementation
        MarketDataCache concurrentCache =
                new MarketDataCache();

        concurrentCache.update("AAPL", newer);
        concurrentCache.update("AAPL", older);

        System.out.println("Concurrent cache:");
        System.out.println(concurrentCache.get("AAPL"));

        // Single-writer implementation
        SingleWriterCache singleWriterCache =
                new SingleWriterCache();

        singleWriterCache.update("AAPL", newer);
        singleWriterCache.update("AAPL", older);

        System.out.println("Single-writer cache:");
        System.out.println(singleWriterCache.get("AAPL"));
    }
}
