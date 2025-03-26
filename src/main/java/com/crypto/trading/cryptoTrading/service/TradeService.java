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

    private static final String BTCUSDT_PAIR = "BTCUSDT";
    private static final String ETHUSDT_PAIR = "ETHUSDT";
    private static final String BUY = "BUY";
    private static final String SELL = "SELL";

    private static final String TRADE_SUCCESSFULLY = "Trade successful";
    private static final String INVALID_TRADE = "Invalid user or trading pair";
    private static final String INSUFFICIENT_USDT = "Insufficient USDT balance";
    private static final String INSUFFICIENT_CRYPTO = "Insufficient crypto balance";

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
            return INVALID_TRADE;
        }

        UserWallet wallet = userWalletOpt.get();
        TradingPair pairData = tradingPairOpt.get();
        double price = type.equalsIgnoreCase(BUY) ? pairData.getAskPrice() : pairData.getBidPrice();
        double cost = price * quantity;

        if (type.equalsIgnoreCase(BUY)) {
            if (wallet.getUsdtBalance() < cost) {
                return INSUFFICIENT_USDT;
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance() - cost);
            updateCryptoBalance(wallet, pair, quantity);
        } else {
            if (!hasSufficientCrypto(wallet, pair, quantity)) {
                return INSUFFICIENT_CRYPTO;
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance() + cost);
            updateCryptoBalance(wallet, pair, -quantity);
        }

        walletRepository.save(wallet);
        transactionRepository.save(new TradeTransaction(null, userId, pair, type, price, quantity, LocalDateTime.now()));
        return TRADE_SUCCESSFULLY;
    }

    private boolean hasSufficientCrypto(UserWallet wallet, String pair, double quantity) {
        return (pair.equals(BTCUSDT_PAIR) && wallet.getBtcBalance() >= quantity) ||
                (pair.equals(ETHUSDT_PAIR) && wallet.getEthBalance() >= quantity);
    }

    private void updateCryptoBalance(UserWallet wallet, String pair, double quantity) {
        if (pair.equals(BTCUSDT_PAIR)) {
            wallet.setBtcBalance(wallet.getBtcBalance() + quantity);
        } else if (pair.equals(ETHUSDT_PAIR)) {
            wallet.setEthBalance(wallet.getEthBalance() + quantity);
        }
    }
}
