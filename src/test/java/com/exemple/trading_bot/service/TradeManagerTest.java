package com.exemple.trading_bot.service;

import com.exemple.trading_bot.model.Asset;
import com.exemple.trading_bot.model.Portfolio;
import com.exemple.trading_bot.model.Trade;
import com.exemple.trading_bot.model.TradeClosure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TradeManagerTest {
    @Autowired
    private TradeManager tradeManager;
    @Autowired
    private PortfolioManager portfolioManager;
    @Autowired
    private PortfolioItemManager portfolioItemManager;
    @Autowired
    private AssetManager assetManager;

    private Portfolio portfolio;
    private Asset BTC;
    private Asset EUR;

    @BeforeEach
    public void init() {
        BTC = assetManager.find("BTC");
        EUR = assetManager.find("EUR");

        portfolio = this.portfolioManager.update(portfolioManager.create("RsiPortfolio")
                .addItem(assetManager.find("BTC"), BigDecimal.ZERO.setScale(8, RoundingMode.HALF_DOWN))
                .addItem(assetManager.find("ETH"), BigDecimal.ZERO.setScale(8, RoundingMode.HALF_DOWN))
                .addItem(assetManager.find("EUR"), BigDecimal.valueOf(1000).setScale(8, RoundingMode.HALF_DOWN))
                .addItem(assetManager.find("USDT"), BigDecimal.ZERO.setScale(8, RoundingMode.HALF_DOWN)));
    }

    @Test
    @Transactional
    void buy() {
        portfolioItemManager.findByPortfolioAndAsset(portfolio, EUR).setQuantity(BigDecimal.valueOf(1000).setScale(8, RoundingMode.HALF_DOWN));

        final BigDecimal quantity = portfolioItemManager.findByPortfolioAndAsset(portfolio, EUR).getQuantity().divide(BigDecimal.valueOf(16000.0), 8, RoundingMode.HALF_DOWN);
        final BigDecimal price = BigDecimal.valueOf(16000.0);
        final Trade trade = tradeManager.create(portfolio, BTC, EUR, price, quantity);

        assertEquals(0.0625, trade.getQuantity().doubleValue());
        // should be 0 but -1 because of the fee
        assertEquals(-1, portfolioItemManager.findByPortfolioAndAsset(portfolio, trade.getSold()).getQuantity().doubleValue());
        assertEquals(0.0625, portfolioItemManager.findByPortfolioAndAsset(portfolio, trade.getBought()).getQuantity().doubleValue());
    }

    @Test
    @Transactional
    void buy_then_sell() {
        portfolioItemManager.findByPortfolioAndAsset(portfolio, EUR).setQuantity(BigDecimal.valueOf(1000).setScale(8, RoundingMode.HALF_DOWN));

        final BigDecimal quantity = portfolioItemManager.findByPortfolioAndAsset(portfolio, EUR).getQuantity().divide(BigDecimal.valueOf(16000.0), 8, RoundingMode.HALF_DOWN);
        final BigDecimal price = BigDecimal.valueOf(16000.0);
        final Trade trade = tradeManager.create(portfolio, BTC, EUR, price, quantity);
        final TradeClosure tradeClosure = tradeManager.close(portfolio, trade, BigDecimal.valueOf(16000));

        assertEquals(0.0625, trade.getQuantity().doubleValue());
        assertEquals(0.0625, tradeClosure.getQuantity().doubleValue());
        // should be 1000 but 998 because of the fees
        assertEquals(998, portfolioItemManager.findByPortfolioAndAsset(portfolio, trade.getSold()).getQuantity().doubleValue());
        assertEquals(0, portfolioItemManager.findByPortfolioAndAsset(portfolio, trade.getBought()).getQuantity().doubleValue());
    }
}