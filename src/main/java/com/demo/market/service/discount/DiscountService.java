package com.demo.market.service.discount;

import com.demo.market.dto.discount.DiscountDtoReq;
import com.demo.market.dto.discount.DiscountDtoResp;

public interface DiscountService {

    DiscountDtoResp add(DiscountDtoReq discountDtoReq);
    DiscountDtoResp update(Long discountId, DiscountDtoReq discountDtoReq);
    DiscountDtoResp get(Long discountId);
    DiscountDtoResp delete(Long discountId);
}
