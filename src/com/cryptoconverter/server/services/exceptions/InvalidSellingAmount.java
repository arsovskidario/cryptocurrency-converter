package com.cryptoconverter.server.services.exceptions;

public class InvalidSellingAmount extends IllegalArgumentException {

    public InvalidSellingAmount() {
        super("Can't have negative amount of currency sold !");
    }
}

