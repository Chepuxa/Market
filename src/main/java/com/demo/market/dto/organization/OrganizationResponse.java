package com.demo.market.dto.organization;

import com.demo.market.dto.product.ProductResponse;
import com.demo.market.dto.user.UserResponse;
import com.demo.market.enums.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationResponse {

    private Long id;
    private String name;
    private String description;
    private String logotype;
    private ActiveStatus status;
    private Set<ProductResponse> products;
    private UserResponse user;
}
