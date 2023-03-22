package com.demo.market.exceptions;

public class NotEnoughCredits extends RuntimeException {

    public NotEnoughCredits() {
        super("Not enough credits on balance");
    }
}
