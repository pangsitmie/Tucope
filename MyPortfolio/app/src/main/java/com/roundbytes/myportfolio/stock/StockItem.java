package com.roundbytes.myportfolio.stock;

import java.util.ArrayList;

public class StockItem {
    private String stockCode;
    private int lot;
    private double stockAvgBuyPrice, stockSubTotalBuyValue;
    private boolean isExpanded;

    private ArrayList<StocksTransactions> transactions;

    public StockItem(){
    }

    public StockItem(String name) {
        this.stockCode = name;
        this.lot = lot;
        this.stockAvgBuyPrice = stockAvgBuyPrice;
        this.stockSubTotalBuyValue = stockSubTotalBuyValue;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public int getLot() {
        return lot;
    }

    public void setLot(int lot) {
        this.lot = lot;
    }

    public double getStockAvgBuyPrice() {
        return stockAvgBuyPrice;
    }

    public void setStockAvgBuyPrice(double stockAvgBuyPrice) {
        this.stockAvgBuyPrice = stockAvgBuyPrice;
    }

    public double getStockSubTotalBuyValue() {
        return stockSubTotalBuyValue;
    }

    public void setStockSubTotalBuyValue(double stockSubTotalBuyValue) {
        this.stockSubTotalBuyValue = stockSubTotalBuyValue;
    }

    public ArrayList<StocksTransactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<StocksTransactions> transactions) {
        this.transactions = transactions;
    }
}
