package com.demo.market.exceptions;

public class InsufficientRights extends RuntimeException {

    public InsufficientRights() {
        super("Insufficient rights");
    }
}
