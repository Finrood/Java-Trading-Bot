package com.exemple.trading_bot.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Trade {
    @Id
    private String id;

    @ManyToOne
    private Asset bought;

    @ManyToOne
    private Asset sold;

    @Column(nullable = false, precision = 38, scale = 8)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 38, scale = 8)
    private BigDecimal price;

    @Column(nullable = false, precision = 38, scale = 8)
    private BigDecimal fee;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    protected Trade() {
    }

    public Trade(Asset bought, Asset sold, BigDecimal price, BigDecimal quantity, BigDecimal fee) {
        this.id = UUID.randomUUID().toString();
        this.bought = bought;
        this.sold = sold;
        this.price = price;
        this.quantity = quantity;
        this.fee = fee;
        this.createdOn = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Asset getBought() {
        return bought;
    }

    public Trade setBought(Asset bought) {
        this.bought = bought;
        return this;
    }

    public Asset getSold() {
        return sold;
    }

    public Trade setSold(Asset sold) {
        this.sold = sold;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Trade setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Trade setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public Trade setFee(BigDecimal fee) {
        this.fee = fee;
        return this;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(id, trade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id='" + id + '\'' +
                ", bought='" + bought + '\'' +
                ", sold='" + sold + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", createdOn=" + createdOn +
                '}';
    }
}
