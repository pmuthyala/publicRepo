package org.example.springbootdemo;
import org.example.springbootdemo.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.servlet.function.ServerResponse.notFound;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    // Inject QuoteService using constructor injection.
    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    // GET /api/quotes/{symbol}
    // Return the quote for the requested symbol.
    @GetMapping("/{symbol}")
    public ResponseEntity<QuoteService.Quote> getQuote(@PathVariable String symbol) {

        if(symbol == null || symbol.isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }
        QuoteService.Quote quote =  quoteService.getQuote(symbol);
        System.out.println(quote);
        if(quote == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quote);
    }

    // POST /api/quotes/{symbol}
    // Accept a Quote object in the request body
    // and update the service.
    @PostMapping("/{symbol}")
    public void updateQuote(@PathVariable String symbol, @RequestBody QuoteService.Quote quote) {
        quoteService.update(symbol, quote);
    }
}

