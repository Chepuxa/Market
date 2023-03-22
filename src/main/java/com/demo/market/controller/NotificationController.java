package com.demo.market.controller;

import com.demo.market.dto.notification.NotificationRequest;
import com.demo.market.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> get() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(notificationService.get(userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> send(@PathVariable String userId, @RequestBody @Valid NotificationRequest notificationRequest) {
        return new ResponseEntity<>(notificationService.send(userId, notificationRequest), HttpStatus.CREATED);
    }
}
