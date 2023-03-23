package com.demo.market.exceptions;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Error {

    @NotNull
    private String message;
    @NotNull
    private Long code;
}