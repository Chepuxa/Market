package com.demo.market.controller;

import com.demo.market.dto.Auth;
import com.demo.market.dto.organization.OrganizationRequest;
import com.demo.market.dto.organization.OrganizationResponse;
import com.demo.market.exceptions.Error;
import com.demo.market.service.organization.OrganizationService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Организации")
@ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {@Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = Error.class))})
@Validated
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @Operation(description = "Получить список организаций текущего пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список получен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrganizationResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getByUser() {
        return new ResponseEntity<>(organizationService.getByUser(new Auth()), HttpStatus.OK);
    }

    @Operation(description = "Создать новую организацию", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Организация создана", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrganizationResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "409", description = "Организация уже существует", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> create(@RequestBody @Valid OrganizationRequest organizationRequest) {
        return new ResponseEntity<>(organizationService.create(new Auth(), organizationRequest), HttpStatus.CREATED);
    }

    @Operation(description = "Получить информацию об организации", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrganizationResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "Организация не найдена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @GetMapping("/{organizationId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long organizationId) {
        return new ResponseEntity<>(organizationService.get(new Auth(), organizationId), HttpStatus.OK);
    }

    @Operation(description = "Изменить статус организации", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус изменен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrganizationResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Организация не найдена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @PatchMapping("/{organizationId}/activate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> activate(@PathVariable Long organizationId) {
        return new ResponseEntity<>(organizationService.activate(organizationId), HttpStatus.OK);
    }

    @Operation(description = "Получить список не подтвержденных организации", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список получен", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrganizationResponse.class))})})
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/inactive")
    public ResponseEntity<?> getInactive() {
        return new ResponseEntity<>(organizationService.getInactive(), HttpStatus.OK);
    }

    @Operation(description = "Удалить организацию", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организация удалена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrganizationResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Организация не найдена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))})})
    @DeleteMapping("/{organizationId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> delete(@PathVariable Long organizationId) {
        return new ResponseEntity<>(organizationService.delete(organizationId), HttpStatus.OK);
    }
}
