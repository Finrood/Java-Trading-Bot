package com.exemple.trading_bot.model;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.num.DecimalNum;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Kline {
    private long openTime;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;
    private double volume;
    private long closeTime;

    public static Kline from(List<String> apiKlineData) {
        return new Kline()
                .setOpenTime(Long.parseLong(apiKlineData.get(0)))
                .setOpenPrice(Double.parseDouble(apiKlineData.get(1)))
                .setHighPrice(Double.parseDouble(apiKlineData.get(2)))
                .setLowPrice(Double.parseDouble(apiKlineData.get(3)))
                .setClosePrice(Double.parseDouble(apiKlineData.get(4)))
                .setVolume(Double.parseDouble(apiKlineData.get(5)))
                .setCloseTime(Long.parseLong(apiKlineData.get(6)));
    }

    public Bar toBar(String interval) {
        long time = Long.valueOf(interval.split("[a-zA-Z]")[0]);
        String timeDuration = interval.substring(interval.length()-1);
        Duration duration = Duration.of(time, ChronoUnit.HOURS); // Default is 1h
        switch (timeDuration) {
            case "m":
                duration = Duration.of(time, ChronoUnit.MINUTES);
                break;
            case "h":
                duration = Duration.of(time, ChronoUnit.HOURS);
                break;
            case "d":
                duration = Duration.of(time, ChronoUnit.DAYS);
                break;
            case "w":
                duration = Duration.ofDays(7);
                break;
            case "M":
                duration = Duration.of(time, ChronoUnit.MONTHS);
                break;
        }
        return BaseBar.builder(DecimalNum::valueOf, Number.class)
                .timePeriod(duration)
                .endTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.getCloseTime()), ZoneId.of("UTC")))
                .openPrice(this.getOpenPrice())
                .highPrice(this.getHighPrice())
                .lowPrice(this.getLowPrice())
                .closePrice(this.getClosePrice())
                .volume(this.getVolume()).build();
    }

    public long getOpenTime() {
        return openTime;
    }

    public Kline setOpenTime(long openTime) {
        this.openTime = openTime;
        return this;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public Kline setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
        return this;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public Kline setHighPrice(double highPrice) {
        this.highPrice = highPrice;
        return this;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public Kline setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
        return this;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public Kline setClosePrice(double closePrice) {
        this.closePrice = closePrice;
        return this;
    }

    public double getVolume() {
        return volume;
    }

    public Kline setVolume(double volume) {
        this.volume = volume;
        return this;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public Kline setCloseTime(long closeTime) {
        this.closeTime = closeTime;
        return this;
    }

    @Override
    public String toString() {
        return "Kline{" +
                "openTime=" + openTime +
                ", openPrice=" + openPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                ", closeTime=" + closeTime +
                '}';
    }
}
