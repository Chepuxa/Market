package com.demo.market.exceptions;

public class OutdatedDiscount extends RuntimeException {

    public OutdatedDiscount() {
        super("Discount is outdated");
    }
}
