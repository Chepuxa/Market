package com.demo.market.controller;

import com.demo.market.dto.Auth;
import com.demo.market.dto.discount.DiscountRequest;
import com.demo.market.dto.discount.DiscountResponse;
import com.demo.market.exceptions.Error;
import com.demo.market.service.discount.DiscountService;
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

@Tag(name = "Скидки")
@ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {@Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = Error.class))})
@Validated
@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @Operation(description = "Получить информацию о скидке", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DiscountResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping("/{discountId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long discountId) {
        return new ResponseEntity<>(discountService.get(new Auth(), discountId), HttpStatus.OK);
    }

    @Operation(description = "Обновить скидку", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка обновлена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DiscountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Скидка или продукт не найдены", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PutMapping("/{discountId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> update(@PathVariable Long discountId, @RequestBody @Valid DiscountRequest discountRequest) {
        return new ResponseEntity<>(discountService.update(discountId, discountRequest), HttpStatus.OK);
    }

    @Operation(description = "Удалить скидку", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка удалена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DiscountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @DeleteMapping("/{discountId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> delete(@PathVariable Long discountId) {
        return new ResponseEntity<>(discountService.delete(discountId), HttpStatus.OK);
    }

    @Operation(description = "Добавить новую скидку", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Скидка создана", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DiscountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Один из продуктов не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> add(@RequestBody @Validated DiscountRequest discountRequest) {
        return new ResponseEntity<>(discountService.add(discountRequest), HttpStatus.CREATED);
    }
}
