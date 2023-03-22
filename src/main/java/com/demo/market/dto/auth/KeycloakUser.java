package com.demo.market.dto.auth;

import lombok.Data;

@Data
public class KeycloakUser {
    private String id;
    private Boolean enabled;
}
