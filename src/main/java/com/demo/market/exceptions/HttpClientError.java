package com.demo.market.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class HttpClientError extends RuntimeException {

    @Getter
    private final HttpStatus status;

    @Getter
    private final String message;

    public HttpClientError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
