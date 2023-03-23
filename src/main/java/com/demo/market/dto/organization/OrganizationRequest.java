package com.demo.market.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema
public class OrganizationRequest {

    @NotBlank
    @Size(min = 1, max = 64)
    private String name;

    @Size(min = 1, max = 128)
    private String description;

    @Size(min = 1, max = 16)
    private String logotype;
}
