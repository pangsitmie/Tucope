package com.roundbytes.myportfolio.stock;

public class StocksTransactions {
    private String date, type;
    private int lot;
    private double price;

    public StocksTransactions(String date, int lot, double price, String type) {
        this.date = date;
        this.lot = lot;
        this.price = price;
        this.type = type;
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

    public int getLot() {
        return lot;
    }

    public void setLot(int lot) {
        this.lot = lot;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
