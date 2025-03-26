package com.crypto.trading.cryptoTrading.controller;


import com.crypto.trading.cryptoTrading.model.TradeTransaction;
import com.crypto.trading.cryptoTrading.model.TradingPair;
import com.crypto.trading.cryptoTrading.model.UserWallet;
import com.crypto.trading.cryptoTrading.repository.TradeTransactionRepository;
import com.crypto.trading.cryptoTrading.repository.TradingPairRepository;
import com.crypto.trading.cryptoTrading.repository.UserWalletRepository;
import com.crypto.trading.cryptoTrading.service.TradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TradingController {
    private final TradingPairRepository pairRepository;
    private final TradeTransactionRepository transactionRepository;
    private final UserWalletRepository walletRepository;
    private final TradeService tradeService;

    public TradingController(TradingPairRepository pairRepository, TradeTransactionRepository transactionRepository,
                             UserWalletRepository walletRepository, TradeService tradeService) {
        this.pairRepository = pairRepository;
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.tradeService = tradeService;
    }

    @GetMapping("/price")
    public List<TradingPair> getLatestPrices() {
        return pairRepository.findAll();
    }

    @PostMapping("/trade")
    public String trade(@RequestParam String userId, @RequestParam String pair,
                        @RequestParam String type, @RequestParam double quantity) {
        return tradeService.trade(userId, pair, type, quantity);
    }

    @GetMapping("/wallet/{userId}")
    public UserWallet getUserWallet(@PathVariable String userId) {
        return walletRepository.findByUserId(userId).orElse(null);
    }

    @GetMapping("/history/{userId}")
    public List<TradeTransaction> getUserHistory(@PathVariable String userId) {
        return transactionRepository.findByUserId(userId);
    }
}
