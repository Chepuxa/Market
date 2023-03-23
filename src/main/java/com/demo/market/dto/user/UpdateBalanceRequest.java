package com.demo.market.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@Schema
public class UpdateBalanceRequest {

    @NotNull
    @Range(min = 0)
    private Double balance;
}
