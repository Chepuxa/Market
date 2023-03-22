package com.demo.market.dto.notification;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NotificationResponse {

    private Long id;
    private String header;
    private String content;
    private Timestamp timestamp;
}
