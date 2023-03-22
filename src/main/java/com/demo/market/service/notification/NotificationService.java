package com.demo.market.service.notification;

import com.demo.market.dto.notification.NotificationRequest;
import com.demo.market.dto.notification.NotificationResponse;

import java.util.Set;

public interface NotificationService {

    Set<NotificationResponse> get(String userId);
    NotificationResponse send(String userId, NotificationRequest notificationRequest);
}
