package com.roundbytes.myportfolio;

public class StockTotal {
    double totalBuyValue, totalCurrentValue;

    public StockTotal(){
        this.totalBuyValue =0.0;
        this.totalCurrentValue =0.0;
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
