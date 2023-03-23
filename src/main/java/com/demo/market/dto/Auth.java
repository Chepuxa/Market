package com.demo.market.dto;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Auth {

    private String userId;
    private Set<String> userRoles;

    public Auth() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userId = authentication.getName();
        userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

}
