package com.exemple.trading_bot.repository;

import com.exemple.trading_bot.model.Portfolio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends CrudRepository<Portfolio, String> {
    Optional<Portfolio> findById(String id);
}
