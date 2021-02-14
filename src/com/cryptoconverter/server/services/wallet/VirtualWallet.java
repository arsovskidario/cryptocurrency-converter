package com.cryptoconverter.server.services.wallet;

import com.cryptoconverter.server.services.currency.Currency;
import com.cryptoconverter.server.services.currency.CurrencyUpdater;
import com.cryptoconverter.server.services.exceptions.CurrencyNotPresentException;
import com.cryptoconverter.server.services.exceptions.InsufficientCashForPurchaseException;
import com.cryptoconverter.server.services.exceptions.InvalidDepositAmount;
import com.cryptoconverter.server.services.transactions.CryptoTransaction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import java.util.Map;
import java.util.HashMap;


public class VirtualWallet {

    private double cashAmount;

    //assetId : Transaction
    private Map<String, LinkedList<CryptoTransaction>> nameToTransaction;

    private static final int TIME_LIMIT = 30;

    public VirtualWallet() {

        this.cashAmount = 0;
        this.nameToTransaction = new HashMap<>();
        CurrencyUpdater.initializeCurrencies();
    }

    public void depositCash(double amount) {
        if (amount < 0) {
            throw new InvalidDepositAmount();
        }

        this.cashAmount += amount;
    }


    /**
     * Sell currency and add the profit to users cash amount.
     * Also remove the currency from the wallet.
     * If amount is bigger than amount of currencies present then all the currencies are sold.
     *
     * @param sellAmount
     * @param currencyName
     */


    public void sellCurrency(double sellAmount, String currencyName) {
        if (sellAmount < 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }

        if (!nameToTransaction.containsKey(currencyName)) {
            throw new CurrencyNotPresentException();
        }

        updateCurrencyPrice();

        List<CryptoTransaction> currencies = nameToTransaction.get(currencyName);
        Iterator<CryptoTransaction> iterator = currencies.listIterator();

        while (iterator.hasNext() && sellAmount > 0) {
            CryptoTransaction transaction = iterator.next();

            double transactionAmount = transaction.getCurrencyAmount();
            if (transactionAmount <= sellAmount) {
                sellAmount -= transactionAmount;
                cashAmount += transactionAmount * transaction.getCurrentPrice();
                iterator.remove();
            } else {
                transaction.removeAmount(sellAmount);
                cashAmount += sellAmount * transaction.getCurrentPrice();
                sellAmount = 0;
            }

        }


    }

    /**
     * Enters assetID and buys desired currency for specified dollar amount
     *
     * @param purchaseCashAmount
     * @param currencyName
     */

    public void buyCurrency(double purchaseCashAmount, String currencyName) {
        if (purchaseCashAmount < 0) {
            throw new InvalidDepositAmount();
        }

        if (purchaseCashAmount > cashAmount) {
            throw new InsufficientCashForPurchaseException();
        }

        updateCurrencyPrice();

        Currency currency = CurrencyUpdater.getCurrency(currencyName);


        double amountBought = purchaseCashAmount / currency.getCurrentPrice();

        cashAmount -= purchaseCashAmount;

        if (!nameToTransaction.containsKey(currencyName)) {
            nameToTransaction.put(currencyName, new LinkedList<>());
        }

        nameToTransaction.get(currencyName).push(new CryptoTransaction(currency.getCurrentPrice(),
                currency, amountBought));

    }


    /**
     * Checks if 30mins have passed since last update,
     * if so updates the currencies
     */
    private void updateCurrencyPrice() {
        Duration duration = Duration.between(CurrencyUpdater.getLastModified(), LocalDateTime.now());
        if (duration.toMinutes() >= TIME_LIMIT) {
            CurrencyUpdater.updateCurrencies();
        }
    }

    /**
     * Displays currencies that can be bought.
     */
    public String listOfferings() {
        StringBuilder result = new StringBuilder();
        for (Currency currency : CurrencyUpdater.getAvailableCurrencies()) {
            result.append(currency.getAssetId()).append(" ").append(currency.getName()).append(" ")
                    .append(currency.getCurrentPrice()).append(System.lineSeparator());
        }

        return result.toString();
    }


    /**
     * Returns details about the overall transactions, name bought price, current price and time bought
     *
     * @return String containing summary
     */


    public String getWalletSummary() {

        StringBuilder result = new StringBuilder();

        result.append("User wallet summary : ").append(System.lineSeparator());

        var transactions = nameToTransaction.values();

        for (var transactionList : transactions) {
            for (CryptoTransaction transaction : transactionList) {
                result.append(transaction.getTransactionSummary());
            }
        }


        return result.toString();

    }


    /**
     * Returns details about the profit/lost based on all transactions
     *
     * @return String containing overall summary
     */
    public String getWalletOverallSummary() {
        StringBuilder result = new StringBuilder();
        result.append("Overall wallet summary").append(System.lineSeparator());
        result.append("Cash balance: ").append(cashAmount).append(System.lineSeparator());

        var transactions = nameToTransaction.values();
        for (var transactionList : transactions) {
            for (CryptoTransaction transaction : transactionList) {
                result.append("Time of purchase: ").append(transaction.getTransactionTime()).append(System.lineSeparator());
                result.append("Currency name: ").append(transaction.getCurrencyName()).append(System.lineSeparator());
                result.append("Value summary: ").append(transaction.calculateDifference()).append(System.lineSeparator());

            }
        }

        return result.toString();
    }

    public double getWalletCash() {
        return cashAmount;
    }

    public void withdrawCash(double amount) {
        if (amount < 0 || cashAmount < amount) {
            throw new IllegalArgumentException("Invalid cash amount");
        }

        cashAmount -= amount;
    }


}
