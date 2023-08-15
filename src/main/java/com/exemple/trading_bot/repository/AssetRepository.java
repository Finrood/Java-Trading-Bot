package com.exemple.trading_bot.repository;

import com.exemple.trading_bot.model.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends CrudRepository<Asset, String> {
    Optional<Asset> findById(String id);
}
