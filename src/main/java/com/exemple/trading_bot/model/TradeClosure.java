package com.exemple.trading_bot.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class TradeClosure {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "trade_id", nullable = false)
    private Trade trade;

    @Column(nullable = false, precision = 38, scale = 8)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 38, scale = 8)
    private BigDecimal price;

    @Column(nullable = false, precision = 38, scale = 8)
    private BigDecimal fee;

    @Column(nullable = false)
    private LocalDateTime closureDate;

    protected TradeClosure() {
    }

    public TradeClosure(Trade trade, BigDecimal quantity, BigDecimal price, BigDecimal fee) {
        this.id = UUID.randomUUID().toString();
        this.trade = trade;
        this.quantity = quantity;
        this.price = price;
        this.fee = fee;
        this.closureDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Trade getTrade() {
        return trade;
    }

    public TradeClosure setTrade(Trade trade) {
        this.trade = trade;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public TradeClosure setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public TradeClosure setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public TradeClosure setFee(BigDecimal fee) {
        this.fee = fee;
        return this;
    }

    public LocalDateTime getClosureDate() {
        return closureDate;
    }

    public TradeClosure setClosureDate(LocalDateTime closureDate) {
        this.closureDate = closureDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeClosure that = (TradeClosure) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TradeClosure{" +
                "id=" + id +
                ", trade=" + trade +
                ", quantity=" + quantity +
                ", price=" + price +
                ", closureDate=" + closureDate +
                '}';
    }
}