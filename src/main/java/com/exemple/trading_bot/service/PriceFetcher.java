package com.exemple.trading_bot.service;

import com.exemple.trading_bot.api.MarketData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

@Service
public class PriceFetcher {
    private static final Logger logger = LoggerFactory.getLogger(PriceFetcher.class);
    public final static BarSeries BarData = new BaseBarSeriesBuilder().withName("BTC_Klines").build();
    private final MarketData marketData;
    public static final String TIMEFRAME = "1h";

    public PriceFetcher(MarketData marketData) {
        this.marketData = marketData;
        fillHistoricalKlinedata(TIMEFRAME, 500);
    }

    @Scheduled(fixedRate = 1000)
    private  void dataFillScheduler() {
        fillRealtimeKlineData(TIMEFRAME);
    }

    public void fillHistoricalKlinedata(String interval, int limit) {
        marketData.getBars("BTCUSDT", interval, limit)
                .forEach(BarData::addBar);
        logger.info("Initially loaded " + BarData.getBarCount() + " bars");
    }

    private void fillRealtimeKlineData(String interval) {
        final Bar btcusdt = marketData.getBars("BTCUSDT", interval, 1).get(0);
        if (BarData.getLastBar().getEndTime().equals(btcusdt.getEndTime())) {
            BarData.addBar(btcusdt, true);
        } else if (BarData.getLastBar().getEndTime().compareTo(btcusdt.getEndTime()) < 0) {
            BarData.addBar(btcusdt);
        }
    }
}
