package org.example;

public record OrderEvent(
        String orderId,
        double price
) implements TradingEvent {
}
