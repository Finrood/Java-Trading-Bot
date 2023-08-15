package com.exemple.trading_bot.repository;

import com.exemple.trading_bot.model.Trade;
import com.exemple.trading_bot.model.TradeClosure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface TradeClosureRepository extends CrudRepository<TradeClosure, String> {
    Optional<TradeClosure> findById(String id);
    Collection<TradeClosure> findByTrade(Trade trade);
}
