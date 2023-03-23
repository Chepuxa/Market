package com.demo.market.service.purchase;

import com.demo.market.dto.Auth;
import com.demo.market.dto.purchase.PurchaseResponse;

import java.util.Set;

public interface PurchaseService {

    Set<PurchaseResponse> getAll(Auth auth);

    Set<PurchaseResponse> getAllByUser(String userId);

    PurchaseResponse get(Auth auth, Long purchaseId);

    PurchaseResponse refund(Auth auth, Long purchaseId);

}
