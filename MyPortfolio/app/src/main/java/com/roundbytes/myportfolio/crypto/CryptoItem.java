package com.roundbytes.myportfolio.crypto;

import java.util.ArrayList;

public class CryptoItem {

    String cryptoCode,cryptoName;
    private double amount, cryptoAvgBuyPrice , cryptoSubTotalBuyValue;
    private boolean expanded;

    private ArrayList<CryptosTransactions> transactions;

    public CryptoItem() {}

    public CryptoItem(String cryptoCode, String cryptoName) {
        this.cryptoCode = cryptoCode;
        this.cryptoName = cryptoName;
        this.amount = 0;
        this.cryptoAvgBuyPrice=0;
        cryptoSubTotalBuyValue = 0;
        this.expanded = false;
    }


    public double getCryptoAvgBuyPrice() {
        return cryptoAvgBuyPrice;
    }

    public void setCryptoAvgBuyPrice(double cryptoAvgBuyPrice) {
        this.cryptoAvgBuyPrice = cryptoAvgBuyPrice;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCryptoSubTotalBuyValue() {
        return cryptoSubTotalBuyValue;
    }

    public void setCryptoSubTotalBuyValue(double cryptoSubTotalBuyValue) {
        this.cryptoSubTotalBuyValue = cryptoSubTotalBuyValue;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public ArrayList<CryptosTransactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<CryptosTransactions> transactions) {
        this.transactions = transactions;
    }
}
