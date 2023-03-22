package com.demo.market.controller;

import com.demo.market.dto.review.ReviewRequest;
import com.demo.market.service.review.ReviewService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/new/{productId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> create(@PathVariable Long productId, @RequestBody @Valid ReviewRequest reviewRequest) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(reviewService.add(productId, userId, reviewRequest), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getAll() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(reviewService.getAll(userId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getAllByUser(@PathVariable String userId) {
        return new ResponseEntity<>(reviewService.getAllWithUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> getAllByProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(reviewService.getAllWithProductId(productId), HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.get(reviewId), HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> update(@PathVariable Long reviewId, @RequestBody @Valid ReviewRequest reviewRequest) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return new ResponseEntity<>(reviewService.update(userId, roles, reviewId, reviewRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> delete(@PathVariable Long reviewId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return new ResponseEntity<>(reviewService.delete(userId, roles, reviewId), HttpStatus.OK);
    }
}
