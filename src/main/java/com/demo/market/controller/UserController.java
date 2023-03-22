package com.demo.market.controller;

import com.demo.market.dto.user.UpdateBalanceRequest;
import com.demo.market.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> get() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(userService.getByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getByUser(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getByUser(userId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/balance")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> updateBalance(@PathVariable String userId, @RequestBody @Valid UpdateBalanceRequest updateBalanceRequest) {
        return new ResponseEntity<>(userService.updateBalance(userId, updateBalanceRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> delete(@PathVariable String userId) {
        return new ResponseEntity<>(userService.delete(userId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/activate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> changeActiveStatus(@PathVariable String userId) {
        return new ResponseEntity<>(userService.changeActiveStatus(userId), HttpStatus.OK);
    }
}
