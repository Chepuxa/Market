package com.demo.market.dto.organization;

import com.demo.market.dto.product.ProductResponse;
import com.demo.market.dto.user.UserResponse;
import com.demo.market.enums.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationResponse {

    private Long id;
    private String name;
    private String logotype;
    private UserResponse user;
    private String description;
    private ActiveStatus status;
    private Set<ProductResponse> products;
}
