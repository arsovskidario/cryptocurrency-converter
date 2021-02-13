package com.cryptoconverter.services.transactions;

import com.cryptoconverter.services.currency.Currency;

import java.time.LocalDateTime;

public class CryptoTransaction {


    private double currencyAmount;
    private double boughtPrice;

    private Currency currency;
    private LocalDateTime transactionTime;


    public CryptoTransaction(Double boughtPrice, Currency currency, double currencyAmount) {
        this.boughtPrice = boughtPrice;
        this.currencyAmount = currencyAmount;
        this.currency = currency;
        transactionTime = LocalDateTime.now();
    }


    public void removeAmount(double currencyAmount) {
        this.currencyAmount -= currencyAmount;
    }

    public double getCurrencyAmount() {
        return currencyAmount;
    }

    public double getCurrentPrice() {
        return currency.getCurrentPrice();
    }


    public double getBoughPrice() {
        return boughtPrice;
    }


    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    /**
     * Calculate the win or loss for the current currency investment.
     *
     * @return investment value
     */


    public double calculateDifference() {
        return currency.getCurrentPrice() - boughtPrice;
    }

    /**
     * Returns transaction summary.
     * Time of purchase.
     * Currency name :    Amount:
     * BoughPrice :
     * CurrentPrice:
     */

    public String getTransactionSummary() {
        return "Transaction summary : " + System.lineSeparator() +
                "Time of purchase : " + transactionTime + System.lineSeparator() +
                "Currency name: " + currency.getName() + "  Amount: " + currencyAmount + System.lineSeparator() +
                "Bought Price: " + boughtPrice + System.lineSeparator() +
                "Current Price: " + currency.getCurrentPrice() + System.lineSeparator();
    }

    public String getCurrencyName() {
        return currency.getName();
    }
}
