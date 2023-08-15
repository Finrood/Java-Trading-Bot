package com.exemple.trading_bot.controller;

import com.exemple.trading_bot.model.Portfolio;
import com.exemple.trading_bot.service.PortfolioManager;
import org.springframework.web.bind.annotation.*;

@RestController
public class PortfolioController {
    private final PortfolioManager portfolioManager;
    public PortfolioController(PortfolioManager portfolioManager) {
        this.portfolioManager = portfolioManager;
    }

    @PostMapping(value = "/portfolio")
    public Portfolio create(@RequestParam String name) {
        return portfolioManager.create(name);
    }

    @GetMapping(value = "/portfolio/{id}")
    public Portfolio find(@PathVariable String id) {
        return portfolioManager.find(id);
    }
}
