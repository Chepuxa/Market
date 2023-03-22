package com.demo.market.controller;

import com.demo.market.dto.auth.LoginForm;
import com.demo.market.dto.auth.RegistrationDtoReq;
import com.demo.market.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginForm loginForm) {
        return authService.login(loginForm);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegistrationDtoReq registrationDtoReq) {
        authService.register(registrationDtoReq);
    }

}