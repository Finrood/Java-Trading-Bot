package com.exemple.trading_bot.repository;

import com.exemple.trading_bot.model.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TradeRepository extends CrudRepository<Trade, String> {
    Optional<Trade> findById(String id);

    @Override
    Set<Trade> findAll();

    @Query("SELECT t FROM Trade t WHERE t.quantity > (SELECT SUM(tc.quantity) FROM TradeClosure tc WHERE tc.trade = t)")
    Set<Trade> findOpenTrades();

    @Query("SELECT t FROM Trade t WHERE t.quantity = (SELECT SUM(tc.quantity) FROM TradeClosure tc WHERE tc.trade = t)")
    Set<Trade> findClosedTrades();
}
