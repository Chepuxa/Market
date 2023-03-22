package com.demo.market.exceptions;

public class AlreadyHasReview extends RuntimeException {

    public AlreadyHasReview() {
        super("This product already has review by this user");
    }
}
