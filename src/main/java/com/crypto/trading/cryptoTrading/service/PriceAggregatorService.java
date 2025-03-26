package com.crypto.trading.cryptoTrading.service;


import com.crypto.trading.cryptoTrading.model.TradingPair;
import com.crypto.trading.cryptoTrading.repository.TradingPairRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceAggregatorService {
    private final TradingPairRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public PriceAggregatorService(TradingPairRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 10000)
    public void fetchAndStoreBestPrices() {
        String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
        String huobiUrl = "https://api.huobi.pro/market/tickers";

        try {
            TradingPair btcPair = getBestPrice("BTCUSDT", binanceUrl, huobiUrl);
            TradingPair ethPair = getBestPrice("ETHUSDT", binanceUrl, huobiUrl);

            repository.save(btcPair);
            repository.save(ethPair);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TradingPair getBestPrice(String pair, String binanceUrl, String huobiUrl) {
        double binanceBid = 65000;
        double binanceAsk = 65050;
        double huobiBid = 64980;
        double huobiAsk = 65030;

        double bestBid = Math.max(binanceBid, huobiBid);
        double bestAsk = Math.min(binanceAsk, huobiAsk);

        return new TradingPair(null, pair, bestBid, bestAsk);
    }
}
