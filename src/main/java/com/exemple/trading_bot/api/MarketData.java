package com.exemple.trading_bot.api;

import com.exemple.trading_bot.model.Kline;
import com.exemple.trading_bot.model.Ticker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.ta4j.core.Bar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketData {
    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3";

    private final RestTemplate restTemplate;

    public MarketData(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Ticker getPrice(String symbol) {
        return restTemplate.getForObject(String.format("%s/ticker/price?symbol=%s",BINANCE_API_URL, symbol), Ticker.class);
    }

    public List<Bar> getBars(String symbol, String interval, int limit) {
        final String jsonString = restTemplate.getForObject(String.format("%s/klines?symbol=%s&interval=%s&limit=%s",BINANCE_API_URL, symbol, interval, limit), String.class);
        Gson gson = new Gson();
        Type klineListType = new TypeToken<List<List<String>>>(){}.getType();
        List<List<String>> stringKlines = gson.fromJson(jsonString, klineListType);
        List<Bar> bars = stringKlines.stream()
                .map(Kline::from)
                .map(kline -> kline.toBar(interval))
                .collect(Collectors.toList());
        return bars;
    }

    public List<Bar> getBars(String symbol, String interval, long endTime, int limit) {
        if (limit<=1000) {
            final String jsonString = restTemplate.getForObject(String.format("%s/klines?symbol=%s&interval=%s&endTime=%s&limit=%s", BINANCE_API_URL, symbol, interval, endTime, limit), String.class);
            Gson gson = new Gson();
            Type klineListType = new TypeToken<List<List<String>>>() {
            }.getType();
            List<List<String>> stringKlines = gson.fromJson(jsonString, klineListType);
            List<Bar> bars = stringKlines.stream()
                    .map(Kline::from)
                    .map(kline -> kline.toBar(interval))
                    .collect(Collectors.toList());
            return bars;
        } else {
            List<Bar> bars = new ArrayList<>();
            long firstEndtime = -1;
            for (int i=1000; i<limit; i=i+1000) {
                String jsonString;
                if (firstEndtime == -1) {
                    jsonString = restTemplate.getForObject(String.format("%s/klines?symbol=%s&interval=%s&endTime=%s&limit=%s", BINANCE_API_URL, symbol, interval, endTime, i), String.class);
                } else {
                    jsonString = restTemplate.getForObject(String.format("%s/klines?symbol=%s&interval=%s&endTime=%s&limit=%s", BINANCE_API_URL, symbol, interval, firstEndtime, i), String.class);
                }
                Gson gson = new Gson();
                Type klineListType = new TypeToken<List<List<String>>>() {
                }.getType();
                List<List<String>> stringKlines = gson.fromJson(jsonString, klineListType);
                List<Bar> tmp = stringKlines.stream()
                        .map(Kline::from)
                        .map(kline -> kline.toBar(interval))
                        .collect(Collectors.toList());
                firstEndtime = tmp.get(0).getEndTime().minusHours(1).toInstant().toEpochMilli();
                bars.addAll(0, tmp);
            }
            System.out.println(bars.size());
            return bars;
        }
    }
}
