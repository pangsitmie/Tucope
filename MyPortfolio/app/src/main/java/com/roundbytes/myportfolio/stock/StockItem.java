package com.roundbytes.myportfolio.stock;

import java.util.ArrayList;

public class StockItem {
    private String stockCode;
    private int lot;
    private double avgBuyPrice, totalBuyValue;
    private boolean isExpanded;

    private ArrayList<StocksTransactions> transactions;

    public StockItem(){
    }

    public StockItem(String name) {
        this.stockCode = name;
        this.lot = lot;
        this.avgBuyPrice = avgBuyPrice;
        this.totalBuyValue = totalBuyValue;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void addTransaction(String date, int lot, double price, String type)
    {
        StocksTransactions newTransaction = new StocksTransactions(date, lot, price, type);
        transactions.add(newTransaction);
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

    public double getAvgBuyPrice() {
        return avgBuyPrice;
    }

    public void setAvgBuyPrice(double avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public double getTotalBuyValue() {
        return totalBuyValue;
    }

    public void setTotalBuyValue(double totalBuyValue) {
        this.totalBuyValue = totalBuyValue;
    }

    public ArrayList<StocksTransactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<StocksTransactions> transactions) {
        this.transactions = transactions;
    }
}
