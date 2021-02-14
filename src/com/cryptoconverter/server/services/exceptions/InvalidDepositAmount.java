package com.cryptoconverter.server.services.exceptions;

public class InvalidDepositAmount extends IllegalArgumentException {

    public InvalidDepositAmount() {
        super("Can't have negative cash deposited!");
    }
}
