package com.demo.market.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String id;
    private String username;
    private String email;
    private Double balance;
}
