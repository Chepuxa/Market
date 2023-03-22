package com.demo.market.dto.organization;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class OrganizationRequest {

    @NotBlank
    @Size(min = 1, max = 64)
    private String name;

    @Size(min = 1, max = 128)
    private String description;

    @Size(min = 1, max = 16)
    private String logotype;
}
