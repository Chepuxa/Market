package com.demo.market.repository;

import com.demo.market.entity.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Set<Notification> findAllByUserId(String userId);
}
