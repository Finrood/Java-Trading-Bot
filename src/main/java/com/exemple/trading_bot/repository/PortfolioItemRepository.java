package com.exemple.trading_bot.repository;

import com.exemple.trading_bot.model.Asset;
import com.exemple.trading_bot.model.Portfolio;
import com.exemple.trading_bot.model.PortfolioItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioItemRepository extends CrudRepository<PortfolioItem, String> {
    Optional<PortfolioItem> findById(String id);
    Optional<PortfolioItem> findByPortfolioAndAsset(Portfolio portfolio, Asset asset);
}
