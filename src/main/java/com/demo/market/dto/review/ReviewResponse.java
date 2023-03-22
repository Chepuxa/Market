package com.demo.market.dto.review;

import com.demo.market.dto.product.ProductResponse;
import com.demo.market.dto.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {

    private Long id;
    private ProductResponse product;
    @JsonIgnoreProperties({"balance"})
    private UserResponse user;
    private String content;
    private Double mark;
}
