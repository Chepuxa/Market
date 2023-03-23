package com.demo.market.dto.purchase;

import com.demo.market.dto.organization.OrganizationResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseResponse {

    private Long id;
    private String name;
    private Long productId;
    private Timestamp buyTime;
    private String description;
    private Double priceWithDiscount;
    private OrganizationResponse organization;
}
