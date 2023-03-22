package com.demo.market.exceptions;

public class InvalidCredentials extends RuntimeException {

    public InvalidCredentials() {
        super("Invalid user credentials");
    }
}
