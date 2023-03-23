package com.demo.market.dto.product;

import com.demo.market.dto.discount.DiscountResponse;
import com.demo.market.dto.organization.OrganizationResponse;
import com.demo.market.dto.review.ReviewResponse;
import com.demo.market.enums.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;


@Data
@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    private Long id;
    private String name;
    private Long amount;
    private String tags;
    private Double price;
    private Double rating;
    private String description;
    private ActiveStatus status;
    private String characteristics;
    private Set<ReviewResponse> reviews;
    private Set<DiscountResponse> discounts;
    private OrganizationResponse organization;
}
