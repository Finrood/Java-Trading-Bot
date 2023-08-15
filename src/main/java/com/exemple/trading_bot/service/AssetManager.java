package com.exemple.trading_bot.service;

import com.exemple.trading_bot.model.Asset;
import com.exemple.trading_bot.repository.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AssetManager {
    private static final Logger logger = LoggerFactory.getLogger(AssetManager.class);
    private final AssetRepository assetRepository;

    public AssetManager(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset find(String id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("No asset with id [%s] could be found", id)));
    }
}
