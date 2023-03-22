package com.demo.market.dto.discount;

import com.demo.market.dto.product.ProductResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscountDtoResp {

    private Long id;
    private Set<ProductResponse> products;
    private Long amount;
    private Timestamp endTime;
}
