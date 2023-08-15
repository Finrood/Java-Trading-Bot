package com.exemple.trading_bot.controller;

import com.exemple.trading_bot.model.Trade;
import com.exemple.trading_bot.service.TradeManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class TradeController {
    private final TradeManager tradeManager;
    public TradeController(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }

    @GetMapping(value = "/trade/{id}")
    public Trade find(@PathVariable String id) {
        return tradeManager.find(id);
    }

    @GetMapping(value = "/trades")
    public Set<Trade> findAll() {
        return tradeManager.findAll();
    }

    @GetMapping(value = "/trades/open")
    public Set<Trade> findOpen() {
        return tradeManager.findOpen();
    }

    @GetMapping(value = "/trades/closed")
    public Set<Trade> findClosed() {
        return tradeManager.findClosed();
    }
}
