package com.demo.market.exceptions;

public class OrganizationAlreadyExists extends RuntimeException {

    public OrganizationAlreadyExists() {
        super("Organization with same name already exists");
    }
}
