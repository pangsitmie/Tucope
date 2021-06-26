package com.roundbytes.myportfolio.stock;

public class StocksTransactions {
    private String date, type, stockCode;
    private double price, lot, pnl, fee, valueBeforeFee, valueAfterFee;
    private boolean expanded;

    public StocksTransactions() {
    }

    public StocksTransactions(double price, double amount, String date, String type, double fee, double valueBeforeFee, double valueAfterFee, double pnl, String stockCode) {
        this.price = price;
        this.lot = amount;
        this.date = date;
        this.type = type;
        this.fee = fee;
        this.valueBeforeFee = valueBeforeFee;
        this.valueAfterFee = valueAfterFee;
        this.pnl = pnl;
        this.stockCode = stockCode;
        this.expanded = false;
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

    public double getLot() {
        return lot;
    }

    public void setLot(double lot) {
        this.lot = lot;
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

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
