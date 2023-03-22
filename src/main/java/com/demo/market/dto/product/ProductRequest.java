package com.demo.market.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRequest {

    @NotBlank
    @Size(min = 1, max = 32)
    private String name;

    @NotBlank
    @Size(min = 1, max = 128)
    private String description;

    @NotNull
    private Long organizationId;

    @NotNull
    private Double price;

    @Min(1)
    @NotNull
    private Long amount;

    @NotBlank
    @Size(min = 1, max = 128)
    private String characteristics;

    @Size(min = 1, max = 128)
    private String tags;
}
