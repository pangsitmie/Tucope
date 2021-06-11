package com.roundbytes.myportfolio;

public class CryptoTotal {
    double totalCryptoBuyValue, totalCryptoCurrentValue;

    public CryptoTotal(){
        this.totalCryptoBuyValue =0.0;
        this.totalCryptoCurrentValue =0.0;
    }

    public void addTransaction(double d)
    {
        totalCryptoBuyValue +=d;
    }

    public double getTotalCryptoBuyValue() {
        return totalCryptoBuyValue;
    }

    public void setTotalCryptoBuyValue(double totalCryptoBuyValue) {
        this.totalCryptoBuyValue = totalCryptoBuyValue;
    }

    public double getTotalCryptoCurrentValue() {
        return totalCryptoCurrentValue;
    }

    public void setTotalCryptoCurrentValue(double totalCryptoCurrentValue) {
        this.totalCryptoCurrentValue = totalCryptoCurrentValue;
    }
}
