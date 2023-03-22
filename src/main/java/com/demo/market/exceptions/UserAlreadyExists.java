package com.demo.market.exceptions;

public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists() {
        super("User already exists");
    }
}
