package com.demo.market.controller;

import com.demo.market.dto.product.ProductRequest;
import com.demo.market.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> get(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.get(productId), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/{productId}/buy")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> buy(@PathVariable Long productId, @RequestParam(required = false) Optional<Long> discount) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(productService.buy(userId, productId, discount), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> update(@PathVariable Long productId, @RequestBody @Valid ProductRequest productRequest) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return new ResponseEntity<>(productService.update(productId, productRequest, userId, roles), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> delete(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.delete(productId), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> add(@RequestBody @Validated ProductRequest productRequest) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new ResponseEntity<>(productService.add(userId, productRequest), HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}/activate")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> activate(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.activate(productId), HttpStatus.OK);
    }
}
