package com.demo.market.dto.purchase;

import com.demo.market.dto.organization.OrganizationResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseResponse {

    private Long id;
    private Long productId;
    private String name;
    private String description;
    private OrganizationResponse organization;
    private Double priceWithDiscount;
    private Timestamp buyTime;
}
