package com.roundbytes.myportfolio.crypto;

public class CryptosTransactions {
    private String date, type, cryptoCode;
    private double price, amount, pnl, fee, valueBeforeFee, valueAfterFee;
    private boolean expanded;

    public CryptosTransactions() {
    }

    public CryptosTransactions(double price, double amount, String date, String type, double fee, double valueBeforeFee, double valueAfterFee, double pnl, String cryptoCode) {
        this.price = price;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.fee = fee;
        this.valueBeforeFee = valueBeforeFee;
        this.valueAfterFee = valueAfterFee;
        this.pnl = pnl;
        this.expanded = false;
        this.cryptoCode = cryptoCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPnl() {
        return pnl;
    }

    public void setPnl(double pnl) {
        this.pnl = pnl;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getValueBeforeFee() {
        return valueBeforeFee;
    }

    public void setValueBeforeFee(double valueBeforeFee) {
        this.valueBeforeFee = valueBeforeFee;
    }

    public double getValueAfterFee() {
        return valueAfterFee;
    }

    public void setValueAfterFee(double valueAfterFee) {
        this.valueAfterFee = valueAfterFee;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getCryptoCode() {
        return cryptoCode;
    }

    public void setCryptoCode(String cryptoCode) {
        this.cryptoCode = cryptoCode;
    }
}
