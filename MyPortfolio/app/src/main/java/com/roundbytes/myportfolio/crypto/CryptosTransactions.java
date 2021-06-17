package com.roundbytes.myportfolio.crypto;

public class CryptosTransactions {
    private String date, type;
    private double price, amount;

    public CryptosTransactions(double price, double amount, String date, String type) {
        this.price = price;
        this.amount = amount;
        this.date = date;
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
}
