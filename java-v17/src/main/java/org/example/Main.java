package org.example;

public class Main {
    public static void main() {
        IO.println(String.format("Hello and welcome!"));

        /**
         * Usage of Record
         */
        MarketData md = new MarketData("AAPL", 432.10, System.nanoTime());
        IO.println(md.symbol());
        IO.println(md.price());

        /** Switch Expression
         * Switch can return a value.
         * A switch expression produces a value and must be exhaustive.
         */
        String orderStatus = "F";
        String status = switch (orderStatus) {
            case "N" -> "NEW";
            case "F" -> {
                IO.println("Filling order...");
                yield "FILLED";
            }
            default -> "other";
        };
        IO.println(status);

        /**
         * Pattern matching for instanceof
         */
        TradingEvent event = new TradeEvent("BUY");
        //...
        if (event instanceof TradeEvent trade) {
            processTrade(trade);
        }
    }
    private static void processTrade(TradeEvent trade) {
        IO.println("event is a TradeEvent "+trade.tradeType());
    }
}

