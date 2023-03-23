package com.demo.market.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema
public class RegistrationRequest {

    @NotBlank
    @Size(min = 3, max = 16)
    private String username;

    @NotBlank
    @Size(min = 8, max = 32)
    private String password;

    @NotBlank
    @Size(min = 6, max = 32)
    @Email
    private String email;

    @NotBlank
    @Size(min = 1, max = 32)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 32)
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean enabled = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean emailVerified = true;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
