package com.crypto.trading.cryptoTrading.repository;


import com.crypto.trading.cryptoTrading.model.TradingPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradingPairRepository extends JpaRepository<TradingPair, Long> {
    Optional<TradingPair> findByPair(String pair);
}
