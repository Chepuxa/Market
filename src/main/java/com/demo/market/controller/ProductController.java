package com.demo.market.controller;

import com.demo.market.dto.Auth;
import com.demo.market.dto.product.ProductRequest;
import com.demo.market.dto.product.ProductResponse;
import com.demo.market.exceptions.Error;
import com.demo.market.service.product.ProductService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Tag(name = "Продукты")
@ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {@Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = Error.class))})
@Validated
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(description = "Добавить новый продукт", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Продукт добавлен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Организация не найдена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> add(@RequestBody @Validated ProductRequest productRequest) {
        return new ResponseEntity<>(productService.add(new Auth(), productRequest), HttpStatus.CREATED);
    }

    @SuppressWarnings("all")
    @Operation(description = "Купить продукт", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт куплен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Скидка или продукт не найдены", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PostMapping("/{productId}/buy")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> buy(@PathVariable Long productId, @RequestParam(required = false) Optional<Long> discount) {
        return new ResponseEntity<>(productService.buy(new Auth(), productId, discount), HttpStatus.OK);
    }

    @Operation(description = "Получить информацию о продукте", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Продукт не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.get(new Auth(), productId), HttpStatus.OK);
    }

    @Operation(description = "Получить список всех продуктов", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список получен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(productService.getAll(new Auth()), HttpStatus.OK);
    }

    @Operation(description = "Обновить продукт", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт обновлен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Продукт или организация не найдены", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> update(@PathVariable Long productId, @RequestBody @Valid ProductRequest productRequest) {
        return new ResponseEntity<>(productService.update(new Auth(), productId, productRequest), HttpStatus.OK);
    }

    @Operation(description = "Удалить продукт", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт удален", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Продукт не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> delete(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.delete(productId), HttpStatus.OK);
    }

    @Operation(description = "Изменить статус продукта", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус изменен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Продукт не найден", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PatchMapping("/{productId}/activate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> activate(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.activate(productId), HttpStatus.OK);
    }
}