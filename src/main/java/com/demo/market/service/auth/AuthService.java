package com.demo.market.service.auth;

import com.demo.market.dto.auth.LoginRequest;
import com.demo.market.dto.auth.RegistrationRequest;

public interface AuthService {

    void register(RegistrationRequest registrationRequest);

    String login(LoginRequest loginRequest);
}
