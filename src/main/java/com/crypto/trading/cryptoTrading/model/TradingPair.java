package com.crypto.trading.cryptoTrading.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TradingPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pair; // BTCUSDT or ETHUSDT
    private double bidPrice; // Highest price someone is willing to buy
    private double askPrice; // Lowest price someone is willing to sell

    public TradingPair(Long id, String pair, double bidPrice, double askPrice) {
        this.id = id;
        this.pair = pair;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public TradingPair() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }
}
