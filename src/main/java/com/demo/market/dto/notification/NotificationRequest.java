package com.demo.market.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRequest {

    @NotNull
    @Size(min = 1, max = 32)
    private String header;

    @NotNull
    @Size(min = 1, max = 64)
    private String content;
}
