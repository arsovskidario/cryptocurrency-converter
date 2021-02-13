package com.cryptoconverter.services.exceptions;

public class InvalidDepositAmount extends IllegalArgumentException {

    public InvalidDepositAmount() {
        super("Can't have negative cash deposited!");
    }
}
