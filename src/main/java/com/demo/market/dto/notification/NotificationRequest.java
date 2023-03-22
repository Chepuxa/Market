package com.demo.market.dto.notification;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NotificationRequest {

    @NotNull
    @Size(min = 1, max = 32)
    private String header;

    @NotNull
    @Size(min = 1, max = 64)
    private String content;
}
