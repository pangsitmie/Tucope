package com.roundbytes.myportfolio;

import java.util.ArrayList;

public class StocksData {
    private String name;
    private int lot, share;
    private double avgBuyPrice, avgBuyValue;
    private boolean isExpanded;

    private ArrayList<StocksTransactions> transactions = new ArrayList<>();

    public StocksData(String name, int lot, double avgBuyPrice, double avgBuyValue) {
        this.name = name;
        this.lot = lot;
        this.share = lot*100;
        this.avgBuyPrice = avgBuyPrice;
        this.avgBuyValue = avgBuyValue;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLot() {
        return lot;
    }

    public void setLot(int lot) {
        this.lot = lot;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public double getAvgBuyPrice() {
        return avgBuyPrice;
    }

    public void setAvgBuyPrice(double avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public double getAvgBuyValue() {
        return avgBuyValue;
    }

    public void setAvgBuyValue(double avgBuyValue) {
        this.avgBuyValue = avgBuyValue;
    }

    public ArrayList<StocksTransactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<StocksTransactions> transactions) {
        this.transactions = transactions;
    }
}
