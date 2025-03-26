package com.crypto.trading.cryptoTrading.repository;


import com.crypto.trading.cryptoTrading.model.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
    Optional<UserWallet> findByUserId(String userId);
}
