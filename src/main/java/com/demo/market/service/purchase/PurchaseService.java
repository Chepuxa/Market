package com.demo.market.service.purchase;

import com.demo.market.dto.purchase.PurchaseResponse;

import java.util.Set;

public interface PurchaseService {
    Set<PurchaseResponse> getAll(String userId);
    PurchaseResponse get(Long purchaseId, String userId, Set<String> roles);
    PurchaseResponse refund(Long purchaseId, String userId, Set<String> roles);

}
