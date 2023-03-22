package com.demo.market.exceptions;

public class UserNotActive extends RuntimeException {
    public UserNotActive() {
        super("User not active");
    }
}
