package com.demo.market.controller;

import com.demo.market.dto.Auth;
import com.demo.market.dto.notification.NotificationRequest;
import com.demo.market.dto.notification.NotificationResponse;
import com.demo.market.exceptions.Error;
import com.demo.market.service.notification.NotificationService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Уведомления")
@ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {@Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = Error.class))})
@Validated
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Operation(description = "Получить все уведомления пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список уведомлений получен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(notificationService.get(new Auth()), HttpStatus.OK);
    }

    @Operation(description = "Отправить уведомление пользователю", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Уведомление отправлено", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> send(@PathVariable String userId, @RequestBody @Valid NotificationRequest notificationRequest) {
        return new ResponseEntity<>(notificationService.send(userId, notificationRequest), HttpStatus.CREATED);
    }
}
