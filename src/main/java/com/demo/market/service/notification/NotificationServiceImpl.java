package com.demo.market.service.notification;

import com.demo.market.dto.Auth;
import com.demo.market.dto.notification.NotificationRequest;
import com.demo.market.dto.notification.NotificationResponse;
import com.demo.market.entity.Notification;
import com.demo.market.entity.User;
import com.demo.market.enums.ActiveStatus;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.InsufficientRights;
import com.demo.market.exceptions.ItemNotFound;
import com.demo.market.mappers.NotificationMapper;
import com.demo.market.repository.NotificationRepository;
import com.demo.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationMapper notificationMapper;

    @Override
    public Set<NotificationResponse> get(Auth auth) {
        User user = userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE)
                .orElseThrow(InsufficientRights::new);
        return notificationMapper.toDtoSet(notificationRepository.findAllByUserId(user.getId()));
    }

    @Override
    public NotificationResponse send(String userId, NotificationRequest notificationRequest) {
        return userRepository.findById(userId)
                .map(usr -> {
                    Notification notification = notificationMapper.toEntity(notificationRequest);
                    notification.setUser(usr);
                    notification.setTimestamp(Timestamp.from(Instant.now()));
                    return notificationMapper.toDto(notificationRepository.save(notification));
                })
                .orElseThrow(() -> new ItemNotFound(Type.USER));
    }
}
