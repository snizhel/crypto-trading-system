package com.crypto.trading.cryptoTrading.service;


import com.crypto.trading.cryptoTrading.model.TradeTransaction;

import com.crypto.trading.cryptoTrading.model.TradingPair;
import com.crypto.trading.cryptoTrading.model.UserWallet;
import com.crypto.trading.cryptoTrading.repository.TradeTransactionRepository;
import com.crypto.trading.cryptoTrading.repository.TradingPairRepository;
import com.crypto.trading.cryptoTrading.repository.UserWalletRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TradeService {
    private final TradingPairRepository pairRepository;
    private final TradeTransactionRepository transactionRepository;
    private final UserWalletRepository walletRepository;

    public TradeService(TradingPairRepository pairRepository, TradeTransactionRepository transactionRepository, UserWalletRepository walletRepository) {
        this.pairRepository = pairRepository;
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public String trade(String userId, String pair, String type, double quantity) {
        Optional<UserWallet> userWalletOpt = walletRepository.findByUserId(userId);
        Optional<TradingPair> tradingPairOpt = pairRepository.findByPair(pair);

        if (userWalletOpt.isEmpty() || tradingPairOpt.isEmpty()) {
            return "Invalid user or trading pair";
        }

        UserWallet wallet = userWalletOpt.get();
        TradingPair pairData = tradingPairOpt.get();
        double price = type.equalsIgnoreCase("BUY") ? pairData.getAskPrice() : pairData.getBidPrice();
        double cost = price * quantity;

        if (type.equalsIgnoreCase("BUY")) {
            if (wallet.getUsdtBalance() < cost) return "Insufficient USDT balance";
            wallet.setUsdtBalance(wallet.getUsdtBalance() - cost);
            if (pair.equals("BTCUSDT")) wallet.setBtcBalance(wallet.getBtcBalance() + quantity);
            else wallet.setEthBalance(wallet.getEthBalance() + quantity);
        } else {
            if ((pair.equals("BTCUSDT") && wallet.getBtcBalance() < quantity) ||
                (pair.equals("ETHUSDT") && wallet.getEthBalance() < quantity)) {
                return "Insufficient crypto balance";
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance() + cost);
            if (pair.equals("BTCUSDT")) wallet.setBtcBalance(wallet.getBtcBalance() - quantity);
            else wallet.setEthBalance(wallet.getEthBalance() - quantity);
        }

        walletRepository.save(wallet);
        transactionRepository.save(new TradeTransaction(null, userId, pair, type, price, quantity, LocalDateTime.now()));
        return "Trade successful";
    }
}
