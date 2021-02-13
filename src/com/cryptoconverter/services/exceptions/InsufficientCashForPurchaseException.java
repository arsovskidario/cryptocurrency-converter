package com.cryptoconverter.services.exceptions;

public class InsufficientCashForPurchaseException extends RuntimeException {

    public InsufficientCashForPurchaseException() {
        super("Not enough money to buy currency");
    }
}
