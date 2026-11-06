package org.example;

public sealed interface TradingEvent
        permits OrderEvent, TradeEvent, CancelEvent {
}
