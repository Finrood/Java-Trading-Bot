package com.exemple.trading_bot.model;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Portfolio {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private Set<PortfolioItem> items;

    protected Portfolio() {
    }

    public Portfolio(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.items = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Portfolio setName(String name) {
        this.name = name;
        return this;
    }

    public Set<PortfolioItem> getItems() {
        return items;
    }

    public Portfolio addItem(Asset asset, BigDecimal quantity) {
        items.add(new PortfolioItem(asset, quantity, this));
        return this;
    }

    public Portfolio updateItem(PortfolioItem portfolioItem) {
        items.remove(portfolioItem);
        items.add(portfolioItem);
        return this;
    }

    public Portfolio setItems(Set<PortfolioItem> items) {
        this.items = items;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(id, portfolio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
