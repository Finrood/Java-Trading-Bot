package com.exemple.trading_bot.service;

import com.exemple.trading_bot.model.*;
import com.exemple.trading_bot.repository.PortfolioRepository;
import com.exemple.trading_bot.repository.TradeClosureRepository;
import com.exemple.trading_bot.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class TradeManager {
    private static final Logger logger = LoggerFactory.getLogger(TradeManager.class);
    private final TradeRepository tradeRepository;
    private final TradeClosureRepository tradeClosureRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioItemManager portfolioItemManager;
    private final double feeRate = 0.001;

    public TradeManager(TradeRepository tradeRepository,
                        TradeClosureRepository tradeClosureRepository,
                        PortfolioRepository portfolioRepository,
                        PortfolioItemManager portfolioItemManager) {
        this.tradeRepository = tradeRepository;
        this.tradeClosureRepository = tradeClosureRepository;
        this.portfolioRepository = portfolioRepository;
        this.portfolioItemManager = portfolioItemManager;
    }

    /**
     * Saves a new trade in the database
     * @return the created trade
     */
    @Transactional
    public Trade create(Portfolio portfolio, Asset bought, Asset sold, BigDecimal price, BigDecimal quantity) {
        quantity = quantity.setScale(8, RoundingMode.HALF_DOWN);
        price = price.setScale(8, RoundingMode.HALF_DOWN);

        if (quantity.compareTo(BigDecimal.ZERO) <= 0 || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException(String.format("Trade with price [%s] and quantity [%s] is useless to execute",
                    price,
                    quantity)
            );
        }

        final PortfolioItem itemBought = portfolioItemManager.findByPortfolioAndAsset(portfolio, bought);
        final PortfolioItem itemSold = portfolioItemManager.findByPortfolioAndAsset(portfolio, sold);

        final BigDecimal soldQuantity = quantity.multiply(price).setScale(8, RoundingMode.HALF_DOWN);
        final BigDecimal fee = soldQuantity.multiply(BigDecimal.valueOf(feeRate));

        if (itemSold.getQuantity().compareTo(soldQuantity) < 0) {
            throw new IllegalStateException(String.format("Not enough [%s] to make the purchase of [%s] [%s] at price [%s]",
                    sold.getLabel(),
                    quantity,
                    bought.getLabel(),
                    price)
            );
        }

        final BigDecimal moneyAfterSell = itemSold.getQuantity().subtract(soldQuantity.add(fee));
        final BigDecimal moneyAfterBought = itemBought.getQuantity().add(quantity);

        itemBought.setQuantity(moneyAfterBought);
        itemSold.setQuantity(moneyAfterSell);
        portfolio.updateItem(itemBought);
        portfolio.updateItem(itemSold);

        logger.trace("Just bought {} {} for {} {}", itemBought.getQuantity(), bought.getLabel(), itemSold.getQuantity(), sold.getLabel());
        portfolioRepository.save(portfolio);
        return tradeRepository.save(new Trade(bought, sold, price, quantity, fee));
    }

    public TradeClosure close(Portfolio portfolio, Trade trade, BigDecimal price) {
        final PortfolioItem itemBought = portfolioItemManager.findByPortfolioAndAsset(portfolio, trade.getSold());
        final PortfolioItem itemSold = portfolioItemManager.findByPortfolioAndAsset(portfolio, trade.getBought());

        final BigDecimal soldQuantity = trade.getQuantity().multiply(price).setScale(8, RoundingMode.HALF_DOWN);
        final BigDecimal fee = soldQuantity.multiply(BigDecimal.valueOf(feeRate));

        final BigDecimal moneyAfterSell = itemSold.getQuantity().subtract(trade.getQuantity());
        final BigDecimal moneyAfterBought = itemBought.getQuantity().add(soldQuantity.subtract(fee));

        itemBought.setQuantity(moneyAfterBought);
        itemSold.setQuantity(moneyAfterSell);
        portfolio.updateItem(itemBought);
        portfolio.updateItem(itemSold);

        logger.trace("Closed trade {} at price {}", trade, price);
        portfolioRepository.save(portfolio);
        return tradeClosureRepository.save(new TradeClosure(trade, trade.getQuantity(), price, fee));
    }

    /**
     * Find a trade by its id in the database
     * @param id the id of the trade
     * @return the Trade with the given id
     * @throws NoSuchElementException when no trade with the given id exists
     */
    @Transactional(readOnly = true)
    public Trade find(String id) throws NoSuchElementException {
        return tradeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("No trade found with id [%s]", id)));
    }

    @Transactional(readOnly = true)
    public Set<Trade> findAll() {
        return tradeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Set<Trade> findOpen() {
        return tradeRepository.findOpenTrades();
    }

    @Transactional(readOnly = true)
    public Set<Trade> findClosed() {
        return tradeRepository.findClosedTrades();
    }
}
