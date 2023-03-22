package com.demo.market.exceptions;

public class RefundTimeExpired extends RuntimeException {

    public RefundTimeExpired() {
        super("Refund time expired");
    }
}
