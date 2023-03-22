package com.demo.market.dto.discount;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.Set;

@Data
public class DiscountDtoReq {

    @NotEmpty
    private Set<Long> productIds;

    @Min(1)
    private Long amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp endTime;
}
