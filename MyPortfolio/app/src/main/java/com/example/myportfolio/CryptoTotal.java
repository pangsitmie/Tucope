package com.example.myportfolio;

public class CryptoTotal {
    double totalBuyValue, totalCurrentValue;

    public CryptoTotal(){
        this.totalBuyValue =1000000.0;
        this.totalCurrentValue =10000000.0;
    }

    public void addTransaction(double d)
    {
        totalBuyValue+=d;
    }

    public double getTotalBuyValue() {
        return totalBuyValue;
    }

    public void setTotalBuyValue(double totalBuyValue) {
        this.totalBuyValue = totalBuyValue;
    }

    public double getTotalCurrentValue() {
        return totalCurrentValue;
    }

    public void setTotalCurrentValue(double totalCurrentValue) {
        this.totalCurrentValue = totalCurrentValue;
    }
}
