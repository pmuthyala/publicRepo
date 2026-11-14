
package org.example.springbootdemo;

import org.example.springbootdemo.service.QuoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuoteController.class)
class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuoteService quoteService;

    @Test
    void shouldReturnQuote() throws Exception {

        when(quoteService.getQuote("AAPL"))
                .thenReturn(new QuoteService.Quote(100, 101));

        mockMvc.perform(get("/api/quotes/AAPL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bid").value(100))
                .andExpect(jsonPath("$.ask").value(101));
    }

    @Test
    void shouldReturn404() throws Exception {
        when(quoteService.getQuote("APPLE"))
                .thenReturn(null);
        mockMvc.perform(get("/api/quotes/APPLE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateQuote() throws Exception {
        QuoteService.Quote quote = new QuoteService.Quote(100,101);

        mockMvc.perform(post("/api/quotes/AAPL")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bid\":100,\"ask\":101}"))
                        .andExpect(status().isOk());

        verify(quoteService, times(1)).update("AAPL", quote);
    }
}
