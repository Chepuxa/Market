package com.demo.market.controller;

import com.demo.market.dto.Auth;
import com.demo.market.dto.review.ReviewRequest;
import com.demo.market.dto.review.ReviewResponse;
import com.demo.market.exceptions.Error;
import com.demo.market.service.review.ReviewService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Отзывы")
@ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {@Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = Error.class))})
@Validated
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Operation(description = "Добавить отзыв к продукту", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отзыв добавлен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Продукт не был ранее приобретен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "409", description = "Отзыв уже присутствует", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Продукт не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PostMapping("/new/{productId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> add(@PathVariable Long productId, @RequestBody @Valid ReviewRequest reviewRequest) {
        return new ResponseEntity<>(reviewService.add(new Auth(), productId, reviewRequest), HttpStatus.CREATED);
    }

    @Operation(description = "Получить список отзывов текущего пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список получен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(reviewService.getAll(new Auth()), HttpStatus.OK);
    }

    @Operation(description = "Получить список отзывов пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список получен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getAllByUser(@PathVariable String userId) {
        return new ResponseEntity<>(reviewService.getAllByUser(userId), HttpStatus.OK);
    }

    @Operation(description = "Получить информацию об отзыве", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.get(new Auth(), reviewId), HttpStatus.OK);
    }

    @Operation(description = "Обновить отзыв", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв обновлен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> update(@PathVariable Long reviewId, @RequestBody @Valid ReviewRequest reviewRequest) {
        return new ResponseEntity<>(reviewService.update(new Auth(), reviewId, reviewRequest), HttpStatus.OK);
    }

    @Operation(description = "Удалить отзыв", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв удален", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> delete(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.delete(new Auth(), reviewId), HttpStatus.OK);
    }
}
