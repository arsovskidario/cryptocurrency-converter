package com.cryptoconverter.server.services.exceptions;

public class InsufficientCashForPurchaseException extends RuntimeException {

    public InsufficientCashForPurchaseException() {
        super("Not enough money to buy currency");
    }
}
