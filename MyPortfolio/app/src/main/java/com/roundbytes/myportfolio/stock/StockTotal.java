package com.roundbytes.myportfolio.stock;

public class StockTotal {
    double totalStockBuyValue, totalStockCurrentValue;

    public StockTotal(){
        this.totalStockBuyValue =0.0;
        this.totalStockCurrentValue =0.0;
    }

    public void addTransaction(double d)
    {
        totalStockBuyValue +=d;
    }

    public double getTotalStockBuyValue() {
        return totalStockBuyValue;
    }

    public void setTotalStockBuyValue(double totalStockBuyValue) {
        this.totalStockBuyValue = totalStockBuyValue;
    }

    public double getTotalStockCurrentValue() {
        return totalStockCurrentValue;
    }

    public void setTotalStockCurrentValue(double totalStockCurrentValue) {
        this.totalStockCurrentValue = totalStockCurrentValue;
    }
}
