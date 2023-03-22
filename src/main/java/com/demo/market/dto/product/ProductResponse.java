package com.demo.market.dto.product;

import com.demo.market.dto.discount.DiscountDtoResp;
import com.demo.market.dto.organization.OrganizationResponse;
import com.demo.market.dto.review.ReviewResponse;
import com.demo.market.enums.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long amount;
    private String characteristics;
    private Set<DiscountDtoResp> discounts;
    private String tags;
    private OrganizationResponse organization;
    private Set<ReviewResponse> reviews;
    private ActiveStatus status;
    private Double rating;
}
