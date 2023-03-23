package com.demo.market.dto.review;

import com.demo.market.dto.product.ProductResponse;
import com.demo.market.dto.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {

    private Long id;
    private Double mark;
    private String content;
    @JsonIgnoreProperties({"balance"})
    private UserResponse user;
    private ProductResponse product;
}
