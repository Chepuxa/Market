package com.demo.market.enums;

public enum Role {

    USER("user"),
    ADMIN("admin");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String withPrefix() {
        return "ROLE_" + role;
    }

    public String withoutPrefix() {
        return role;
    }
}
