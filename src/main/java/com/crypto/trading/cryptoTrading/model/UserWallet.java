package com.crypto.trading.cryptoTrading.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private double usdtBalance;
    private double btcBalance;
    private double ethBalance;

    public UserWallet(Long id, String userId,
                      double usdtBalance, double btcBalance,
                      double ethBalance) {
        this.id = id;
        this.userId = userId;
        this.usdtBalance = usdtBalance;
        this.btcBalance = btcBalance;
        this.ethBalance = ethBalance;
    }

    public UserWallet() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getUsdtBalance() {
        return usdtBalance;
    }

    public void setUsdtBalance(double usdtBalance) {
        this.usdtBalance = usdtBalance;
    }

    public double getBtcBalance() {
        return btcBalance;
    }

    public void setBtcBalance(double btcBalance) {
        this.btcBalance = btcBalance;
    }

    public double getEthBalance() {
        return ethBalance;
    }

    public void setEthBalance(double ethBalance) {
        this.ethBalance = ethBalance;
    }
}
