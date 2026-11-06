package org.example;

public record TradeEvent(
        String tradeType
) implements TradingEvent {}
