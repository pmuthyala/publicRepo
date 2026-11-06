package org.example;

public record CancelEvent(
        String orderId
) implements TradingEvent {
}
