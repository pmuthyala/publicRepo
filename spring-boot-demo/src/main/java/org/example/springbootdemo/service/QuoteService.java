package org.example.springbootdemo.service;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuoteService {

    private final Map<String, Quote> quotes =
            new ConcurrentHashMap<>();

    public record Quote(long bid, long ask) {}

    public void update(String symbol, Quote quote) {
        quotes.put(symbol, quote);
    }

    public Quote getQuote(String symbol) {
        return quotes.get(symbol);
    }
}