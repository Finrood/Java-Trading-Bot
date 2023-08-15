package com.exemple.trading_bot.service.strategy;

import com.exemple.trading_bot.model.Asset;
import com.exemple.trading_bot.model.Portfolio;
import com.exemple.trading_bot.model.Trade;
import com.exemple.trading_bot.service.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RSIStrategy implements ApplicationRunner {
    private final TradeManager tradeManager;
    private final AssetManager assetManager;
    private final Map<Integer, Portfolio> portfolios = new HashMap<>();
    private final PortfolioManager portfolioManager;
    private final PortfolioItemManager portfolioItemManager;
    //Key is the RSI period and trade is the current trade for that period
    private final Map<Integer, Trade> currentTrades = new HashMap<>();
    private final List<Integer> rsiPeriods = List.of(2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30);

    public RSIStrategy(TradeManager tradeManager,
                       AssetManager assetManager,
                       PortfolioManager portfolioManager,
                       PortfolioItemManager portfolioItemManager) {
        this.tradeManager = tradeManager;
        this.assetManager = assetManager;
        this.portfolioManager = portfolioManager;
        this.portfolioItemManager = portfolioItemManager;

        rsiPeriods.forEach(integer -> portfolios.put(integer, portfolioManager.create(String.format("RsiPortfolio-%s", integer))));
    }

    public void run(ApplicationArguments args) {
        rsiPeriods.forEach(integer -> this.portfolioManager.update(portfolios.get(integer)
                .addItem(assetManager.find("BTC"), BigDecimal.ZERO.setScale(8, RoundingMode.HALF_DOWN))
                .addItem(assetManager.find("ETH"), BigDecimal.ZERO.setScale(8, RoundingMode.HALF_DOWN))
                .addItem(assetManager.find("EUR"), BigDecimal.valueOf(1000).setScale(8, RoundingMode.HALF_DOWN))
                .addItem(assetManager.find("USDT"), BigDecimal.ZERO.setScale(8, RoundingMode.HALF_DOWN))));
    }

    @Scheduled(fixedRate = 1000, initialDelay = 30000)
    private  void dataFillScheduler() {
        Rsi(2);
        Rsi(3);
        Rsi(4);
        Rsi(5);
        Rsi(6);
        Rsi(7);
        Rsi(8);
        Rsi(9);
        Rsi(10);
        Rsi(11);
        Rsi(12);
        Rsi(13);
        Rsi(14);
        Rsi(15);
        Rsi(16);
        Rsi(17);
        Rsi(18);
        Rsi(19);
        Rsi(21);
        Rsi(22);
        Rsi(23);
        Rsi(24);
        Rsi(25);
        Rsi(26);
        Rsi(27);
        Rsi(28);
        Rsi(29);
        Rsi(30);
    }

    private Strategy initStrategy(int period) {
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(PriceFetcher.BarData);

        final RSIIndicator rsiIndicator = new RSIIndicator(closePrice, period);

        final Rule entryRule = new CrossedDownIndicatorRule(rsiIndicator, 25);
        final Rule exitRule = new CrossedUpIndicatorRule(rsiIndicator, 75);

        return new BaseStrategy(entryRule, exitRule);
    }

    private void Rsi(int period) {
        final Strategy strategy = initStrategy(period);

        final double price = PriceFetcher.BarData.getLastBar().getClosePrice().doubleValue();

        if (strategy.shouldEnter(PriceFetcher.BarData.getEndIndex()) && currentTrades.get(period) == null) {
            final Asset btc = assetManager.find("BTC");
            final Asset eur = assetManager.find("EUR");

            currentTrades.put(period, tradeManager.create(
                    portfolios.get(period),
                    btc,
                    eur,
                    BigDecimal.valueOf(price),
                    portfolioItemManager.findByPortfolioAndAsset(portfolios.get(period), eur)
                            .getQuantity()
                            .divide(BigDecimal.valueOf(price), RoundingMode.HALF_DOWN)));
        } else if (strategy.shouldExit(PriceFetcher.BarData.getEndIndex()) && currentTrades.get(period) != null) {
            tradeManager.close(portfolios.get(period), currentTrades.get(period), BigDecimal.valueOf(price));
            System.out.println("Just sold");
            currentTrades.put(period, null);
        }
    }
}
