package com.example.myportfolio;

import java.util.ArrayList;

public class CryptoList {
    private String cryptoCode, cryptoName;
    private double avgBuyPrice, totalBuyValue, amount, unrealized, currentValue;
    private boolean isExpanded;

    ArrayList<CryptosTransactions> transactions = new ArrayList<CryptosTransactions>();


    public CryptoList(String cryptoCode) {
        this.cryptoCode = cryptoCode;
        this.avgBuyPrice =0;
        this.totalBuyValue =0;
        this.amount =0;
        this.unrealized =0;
        this.currentValue =0;
        this.isExpanded = false;
    }
    public void addTransaction(double price, double amount, String date, String type)
    {
        CryptosTransactions newTransaction = new CryptosTransactions(price, amount, date, type);
        transactions.add(newTransaction);
        // TODO: 5/29/2021  begitu addTransaction haruse avgbuyprice totalbuyvalue dll iku berubah buat code e dibawah ini
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getCryptoCode() {
        return cryptoCode;
    }

    public void setCryptoCode(String cryptoCode) {
        this.cryptoCode = cryptoCode;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getUnrealized() {
        return unrealized;
    }

    public void setUnrealized(double unrealized) {
        this.unrealized = unrealized;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public ArrayList<CryptosTransactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<CryptosTransactions> transactions) {
        this.transactions = transactions;
    }
}
