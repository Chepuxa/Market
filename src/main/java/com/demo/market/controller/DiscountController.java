package com.demo.market.controller;

import com.demo.market.dto.discount.DiscountDtoReq;
import com.demo.market.service.discount.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@Validated
@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @GetMapping("/{discountId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long discountId) {
        return new ResponseEntity<>(discountService.get(discountId), HttpStatus.OK);
    }

    @PutMapping("/{discountId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> update(@PathVariable Long discountId, @RequestBody @Valid DiscountDtoReq discountDtoReq) {
        return new ResponseEntity<>(discountService.update(discountId, discountDtoReq), HttpStatus.OK);
    }

    @DeleteMapping("/{discountId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> delete(@PathVariable Long discountId) {
        return new ResponseEntity<>(discountService.delete(discountId), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> add(@RequestBody @Validated DiscountDtoReq discountDtoReq) {
        return new ResponseEntity<>(discountService.add(discountDtoReq), HttpStatus.CREATED);
    }
}
