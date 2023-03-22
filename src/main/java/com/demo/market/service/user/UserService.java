package com.demo.market.service.user;

import com.demo.market.dto.user.UpdateBalanceRequest;
import com.demo.market.dto.user.UserResponse;

public interface UserService {
    UserResponse get(String userId);
    UserResponse getByUser(String userId);
    UserResponse updateBalance(String userId, UpdateBalanceRequest updateBalanceRequest);
    UserResponse delete(String userId);
    UserResponse changeActiveStatus(String userId);
}
