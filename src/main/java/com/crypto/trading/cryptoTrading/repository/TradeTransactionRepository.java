package com.crypto.trading.cryptoTrading.repository;


import com.crypto.trading.cryptoTrading.model.TradeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, Long> {
    List<TradeTransaction> findByUserId(String userId);
}
