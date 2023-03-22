package com.demo.market.exceptions;

public class ProductNotOwned extends RuntimeException {
    public ProductNotOwned() {
        super("Product was not purchased before");
    }
}
