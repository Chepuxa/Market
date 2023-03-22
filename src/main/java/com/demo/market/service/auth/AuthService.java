package com.demo.market.service.auth;

import com.demo.market.dto.auth.LoginForm;
import com.demo.market.dto.auth.RegistrationDtoReq;

public interface AuthService {

    void register(RegistrationDtoReq registrationDtoReq);
    String login(LoginForm loginForm);
}
