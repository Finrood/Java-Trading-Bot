package com.exemple.trading_bot.controller;

import com.exemple.trading_bot.api.MarketData;
import com.exemple.trading_bot.model.Ticker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {
    private final MarketData marketData;
    public CryptoController(MarketData marketData) {
        this.marketData = marketData;
    }

    @GetMapping(value = "/price/{symbol}")
    public Ticker getPrice(@PathVariable String symbol) {
        return marketData.getPrice(symbol);
    }
}
