package com.demo.market.controller;

import com.demo.market.dto.organization.OrganizationRequest;
import com.demo.market.service.organization.OrganizationService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getByUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(organizationService.getByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/{organizationId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long organizationId) {
        return new ResponseEntity<>(organizationService.get(organizationId), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> create(@RequestBody @Valid OrganizationRequest organizationRequest) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(organizationService.create(userId, organizationRequest), HttpStatus.CREATED);
    }

    @PatchMapping("/{organizationId}/activate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> activate(@PathVariable Long organizationId) {
        return new ResponseEntity<>(organizationService.activate(organizationId), HttpStatus.OK);
    }

    @GetMapping("/applications")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getInactive() {
        return new ResponseEntity<>(organizationService.getInactive(), HttpStatus.OK);
    }

    @DeleteMapping("/{organizationId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> delete(@PathVariable Long organizationId) {
        return new ResponseEntity<>(organizationService.delete(organizationId), HttpStatus.OK);
    }
}
