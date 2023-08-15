package com.exemple.trading_bot.service;

import com.exemple.trading_bot.model.Portfolio;
import com.exemple.trading_bot.repository.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class PortfolioManager {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioManager.class);
    private final PortfolioRepository portfolioRepository;

    public PortfolioManager(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * Saves a new trade in the database
     * @return the created trade
     */
    @Transactional
    public Portfolio create(String name) {
        return portfolioRepository.save(new Portfolio(name));
    }

    @Transactional
    public Portfolio update(Portfolio portfolio) {
        if (!portfolioRepository.existsById(portfolio.getId())) {
            throw new IllegalStateException("Can not update a portfolio that doesn't already exist");
        }
        return portfolioRepository.save(portfolio);
    }

    @Transactional(readOnly = true)
    public Portfolio find(String id) {
        return portfolioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("No portfolio with id [%s] could be found", id)));
    }
}
