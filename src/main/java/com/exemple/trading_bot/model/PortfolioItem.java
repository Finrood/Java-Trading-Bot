package com.exemple.trading_bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
public class PortfolioItem {
    @Id
    private String id;

    @ManyToOne
    private Asset asset;

    @Column(nullable = false, precision = 38, scale = 8)
    private BigDecimal quantity;

    @ManyToOne
    private Portfolio portfolio;

    protected PortfolioItem() {
    }

    public PortfolioItem(Asset asset, BigDecimal quantity, Portfolio portfolio) {
        this.id = UUID.randomUUID().toString();
        this.asset = asset;
        this.quantity = quantity;
        this.portfolio = portfolio;
    }

    public String getId() {
        return id;
    }

    public Asset getAsset() {
        return asset;
    }

    public PortfolioItem setAsset(Asset asset) {
        this.asset = asset;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public PortfolioItem setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public PortfolioItem setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioItem that = (PortfolioItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PortfolioItem{" +
                "id='" + id + '\'' +
                ", asset=" + asset +
                ", quantity=" + quantity +
                ", portfolio=" + portfolio +
                '}';
    }
}
