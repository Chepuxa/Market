package com.demo.market.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Schema
public class ReviewRequest {

    @Size(min = 1, max = 128)
    private String content;

    @NotNull
    @Range(min = 0, max = 5)
    private Double mark;
}
