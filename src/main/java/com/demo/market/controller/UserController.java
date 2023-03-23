package com.demo.market.controller;

import com.demo.market.dto.Auth;
import com.demo.market.dto.user.UpdateBalanceRequest;
import com.demo.market.dto.user.UserResponse;
import com.demo.market.exceptions.Error;
import com.demo.market.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Пользователи")
@ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {@Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = Error.class))})
@Validated
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(description = "Получить информацию о текущем пользователе", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getByUser() {
        return new ResponseEntity<>(userService.getByUser(new Auth()), HttpStatus.OK);
    }

    @Operation(description = "Получить информацию о пользователе", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> get(@PathVariable String userId) {
        return new ResponseEntity<>(userService.get(userId), HttpStatus.OK);
    }

    @Operation(description = "Обновить баланс пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Баланс обновлен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PatchMapping("/{userId}/balance")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> updateBalance(@PathVariable String userId, @RequestBody @Valid UpdateBalanceRequest updateBalanceRequest) {
        return new ResponseEntity<>(userService.updateBalance(userId, updateBalanceRequest), HttpStatus.OK);
    }

    @Operation(description = "Удалить пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь удален", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> delete(@PathVariable String userId) {
        return new ResponseEntity<>(userService.delete(userId), HttpStatus.OK);
    }

    @Operation(description = "Изменить статус пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус изменен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PatchMapping("/{userId}/activate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> changeActiveStatus(@PathVariable String userId) {
        return new ResponseEntity<>(userService.changeActiveStatus(userId), HttpStatus.OK);
    }
}
