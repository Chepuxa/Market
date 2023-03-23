package com.demo.market.service.discount;

import com.demo.market.dto.Auth;
import com.demo.market.dto.discount.DiscountRequest;
import com.demo.market.dto.discount.DiscountResponse;

public interface DiscountService {

    DiscountResponse add(DiscountRequest discountRequest);

    DiscountResponse update(Long discountId, DiscountRequest discountRequest);

    DiscountResponse get(Auth auth, Long discountId);

    DiscountResponse delete(Long discountId);
}
