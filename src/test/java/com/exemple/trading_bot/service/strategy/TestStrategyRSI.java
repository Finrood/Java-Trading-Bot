package com.exemple.trading_bot.service.strategy;

import com.exemple.trading_bot.api.MarketData;
import com.exemple.trading_bot.service.PriceFetcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.ta4j.core.*;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestStrategyRSI {
    @Autowired
    private MarketData marketData;
    private final BarSeries series = new BaseBarSeries();

    @BeforeAll
    void init() {
        marketData.getBars("BTCUSDT", PriceFetcher.TIMEFRAME, Instant.parse("2023-01-01T00:59:59.000000000Z").toEpochMilli(), 1200)
                .forEach(series::addBar);
    }

    @Test
    void rsi() {
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        RSIIndicator rsiIndicator = new RSIIndicator(closePrice, 14);
        assertEquals(42.406, rsiIndicator.getValue(series.getEndIndex()).floatValue(), 0.01);

        Rule entryRule = new CrossedDownIndicatorRule(rsiIndicator, 30);
        Rule exitRule = new CrossedUpIndicatorRule(rsiIndicator, 70);

        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        BarSeriesManager seriesManager = new BarSeriesManager(series);

        TradingRecord tradingRecord = seriesManager.run(strategy);
        evaluateStrategy(tradingRecord);

        for (int i=2; i<30; i++) {
            rsiIndicator = new RSIIndicator(closePrice, i);
            entryRule = new CrossedDownIndicatorRule(rsiIndicator, 25);
            exitRule = new CrossedUpIndicatorRule(rsiIndicator, 75);

            strategy = new BaseStrategy(entryRule, exitRule);

            tradingRecord = seriesManager.run(strategy);
            System.out.println("For RSI " + i);
            evaluateStrategy(tradingRecord);
            System.out.println("-------------------------------------------------------");
            System.out.println();
        }
    }

    private void evaluateStrategy(TradingRecord tradingRecord) {
        long winningTrades = tradingRecord.getPositions().stream()
                .filter(trade -> trade.getProfit().isGreaterThan(DecimalNum.valueOf(0)))
                .count();
        long losingTrades = tradingRecord.getPositions().size() - winningTrades;

        double totalProfit = tradingRecord.getPositions()
                .stream()
                .mapToDouble(position -> position.getProfit().doubleValue())
                .sum();
        double tradingFees = 0.001;
        double totalProfit2 = tradingRecord.getPositions()
                .stream()
                .mapToDouble(position -> position.getProfit().doubleValue() - (position.getProfit().doubleValue() * tradingFees))
                .sum();
        System.out.println("Number of winning trades: " + winningTrades);
        System.out.println("Number of losing trades: " + losingTrades);
        System.out.println("Total profit without fees: " + totalProfit);
        System.out.println("Total profit with fees: " + totalProfit2);
    }
}