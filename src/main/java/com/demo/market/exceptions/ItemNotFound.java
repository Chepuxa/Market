package com.demo.market.exceptions;

import com.demo.market.enums.Type;

public class ItemNotFound extends RuntimeException {

    public ItemNotFound(Type type) {
        super(type + " not found");
    }
}
