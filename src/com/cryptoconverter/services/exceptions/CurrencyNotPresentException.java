package com.cryptoconverter.services.exceptions;

public class CurrencyNotPresentException extends RuntimeException {

    public CurrencyNotPresentException() {
        super("This currency is not present in the wallet!");
    }
}
