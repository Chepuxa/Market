package com.demo.market.dto.auth;

import lombok.Data;

@Data
public class ChangePasswordForm {
    private Boolean temporary = false;
    private String type = "password";
    private String value;

    public ChangePasswordForm(String value) {
        this.value = value;
    }
}
