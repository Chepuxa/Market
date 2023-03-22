package com.demo.market.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class UpdateBalanceRequest {

    @NotNull
    @Range(min = 0)
    private Double balance;
}
