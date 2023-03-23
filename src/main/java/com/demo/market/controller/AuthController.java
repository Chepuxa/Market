package com.demo.market.controller;

import com.demo.market.dto.auth.KeycloakAuthResponse;
import com.demo.market.dto.auth.LoginRequest;
import com.demo.market.dto.auth.RegistrationRequest;
import com.demo.market.exceptions.Error;
import com.demo.market.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Авторизация")
@ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {@Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = Error.class))})
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(description = "Авторизация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = KeycloakAuthResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Авторизационные данные не верны", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "401", description = "Пользователь не активен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(description = "Регистрация", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким username/email уже существует", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PostMapping("/register")
    public void register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        authService.register(registrationRequest);
    }

}