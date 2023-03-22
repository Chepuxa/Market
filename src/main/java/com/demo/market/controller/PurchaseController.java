package com.demo.market.controller;

import com.demo.market.service.purchase.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getAll() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(purchaseService.getAll(userId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getAllByUser(@PathVariable String userId) {
        return new ResponseEntity<>(purchaseService.getAll(userId), HttpStatus.OK);
    }

    @GetMapping("/{purchaseId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long purchaseId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return new ResponseEntity<>(purchaseService.get(purchaseId, userId, roles), HttpStatus.OK);
    }

    @DeleteMapping("/{purchaseId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> refund(@PathVariable Long purchaseId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return new ResponseEntity<>(purchaseService.refund(purchaseId, userId, roles), HttpStatus.OK);
    }
}
