package com.demo.market.service.product;

import com.demo.market.dto.product.ProductRequest;
import com.demo.market.dto.product.ProductResponse;
import com.demo.market.dto.purchase.PurchaseResponse;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface ProductService {

    PurchaseResponse buy(String userId, Long productId, Optional<Long> discountId);

    ProductResponse add(String userId, ProductRequest productRequest);

    ProductResponse update(Long productId, ProductRequest productRequest, String userId, Set<String> userRoles);

    ProductResponse get(Long productId);

    Set<ProductResponse> getAll();

    Set<ProductResponse> getByOrganizationId(Long organizationId);

    ProductResponse delete(Long productId);

    ProductResponse activate(Long productID);
}
