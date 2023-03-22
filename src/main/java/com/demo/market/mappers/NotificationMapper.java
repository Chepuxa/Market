package com.demo.market.mappers;

import com.demo.market.dto.notification.NotificationRequest;
import com.demo.market.dto.notification.NotificationResponse;
import com.demo.market.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Named("NotificationMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    NotificationResponse toDto(Notification notification);

    Notification toEntity(NotificationRequest notificationRequest);

    Set<NotificationResponse> toDtoSet(Set<Notification> notificationEntitySet);
}