package com.demo.market.dto.discount;

import com.demo.market.dto.product.ProductResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscountResponse {

    private Long id;
    private Long amount;
    private Timestamp endTime;
    private Set<ProductResponse> products;
}
