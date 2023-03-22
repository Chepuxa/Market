package com.demo.market.exceptions;

public class ProductOutOfStock extends RuntimeException {

    public ProductOutOfStock() {
        super("Product out of stock");
    }
}
