package com.exemple.trading_bot.service;

import com.exemple.trading_bot.model.Asset;
import com.exemple.trading_bot.model.Portfolio;
import com.exemple.trading_bot.model.PortfolioItem;
import com.exemple.trading_bot.repository.PortfolioItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class PortfolioItemManager {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioItemManager.class);
    private final PortfolioItemRepository portfolioItemRepository;

    public PortfolioItemManager(PortfolioItemRepository portfolioItemRepository) {
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Transactional
    public PortfolioItem create(Portfolio portfolio, Asset asset, BigDecimal quantity) {
        return portfolioItemRepository.save(new PortfolioItem(asset, quantity, portfolio));
    }

    public PortfolioItem findByPortfolioAndAsset(Portfolio portfolio, Asset asset) {
        return portfolioItemRepository.findByPortfolioAndAsset(portfolio, asset)
                .orElseThrow(() -> new NoSuchElementException(String.format("No portfolio item with portfolio [%s] and asset [%s] could be found", portfolio.getId(), asset.getId())));
    }
}
