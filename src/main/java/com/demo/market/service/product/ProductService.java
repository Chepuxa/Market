package com.demo.market.service.product;

import com.demo.market.dto.Auth;
import com.demo.market.dto.product.ProductRequest;
import com.demo.market.dto.product.ProductResponse;
import com.demo.market.dto.purchase.PurchaseResponse;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface ProductService {

    PurchaseResponse buy(Auth auth, Long productId, Optional<Long> discountId);

    ProductResponse add(Auth auth, ProductRequest productRequest);

    ProductResponse update(Auth auth, Long productId, ProductRequest productRequest);

    ProductResponse get(Auth auth, Long productId);

    Set<ProductResponse> getAll(Auth auth);

    ProductResponse delete(Long productId);

    ProductResponse activate(Long productID);
}
