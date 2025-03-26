package com.crypto.trading.cryptoTrading.service;

import com.crypto.trading.cryptoTrading.model.TradingPair;
import com.crypto.trading.cryptoTrading.repository.TradingPairRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceAggregatorService {
    private final TradingPairRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(PriceAggregatorService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public PriceAggregatorService(TradingPairRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 10000)
    public void fetchAndStoreBestPrices() {
        logger.info("Fetching latest crypto prices from Binance & Huobi...");
        String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
        String huobiUrl = "https://api.huobi.pro/market/tickers";

        try {
            TradingPair btcPair = getBestPrice("BTCUSDT");
            TradingPair ethPair = getBestPrice("ETHUSDT");

            repository.save(btcPair);
            repository.save(ethPair);
            logger.info("Updated prices stored in database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TradingPair getBestPrice(String pair) {
        RestTemplate restTemplate = new RestTemplate();

        String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker?symbol=" + pair;
        String huobiUrl = "https://api.huobi.pro/market/tickers";

        try {
            // Fetch Binance Prices
            String binanceResponse = restTemplate.getForObject(binanceUrl, String.class);
            JSONObject binanceJson = new JSONObject(binanceResponse);
            double binanceBid = binanceJson.getDouble("bidPrice");
            double binanceAsk = binanceJson.getDouble("askPrice");

            // Fetch Huobi Prices
            String huobiResponse = restTemplate.getForObject(huobiUrl, String.class);
            JSONObject huobiJson = new JSONObject(huobiResponse);
            JSONArray tickers = huobiJson.getJSONArray("data");

            double huobiBid = 0, huobiAsk = 0;
            for (int i = 0; i < tickers.length(); i++) {
                JSONObject ticker = tickers.getJSONObject(i);
                if (ticker.getString("symbol").equalsIgnoreCase(pair.toLowerCase())) {
                    huobiBid = ticker.getDouble("bid");
                    huobiAsk = ticker.getDouble("ask");
                    break;
                }
            }

            // Select the best bid & ask prices
            double bestBid = Math.max(binanceBid, huobiBid);
            double bestAsk = Math.min(binanceAsk, huobiAsk);

            logger.info("Best price for {}: Bid = {}, Ask = {}", pair, bestBid, bestAsk);

            return new TradingPair(null, pair, bestBid, bestAsk);

        } catch (Exception e) {
            logger.error("Error fetching prices for {}: {}", pair, e.getMessage());
            // Return default values if API fails
            return new TradingPair(null, pair, 0, 0);
        }
    }
}
