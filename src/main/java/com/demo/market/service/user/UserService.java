package com.demo.market.service.user;

import com.demo.market.dto.Auth;
import com.demo.market.dto.user.UpdateBalanceRequest;
import com.demo.market.dto.user.UserResponse;

public interface UserService {

    UserResponse get(String userId);

    UserResponse getByUser(Auth auth);

    UserResponse updateBalance(String userId, UpdateBalanceRequest updateBalanceRequest);

    UserResponse delete(String userId);

    UserResponse changeActiveStatus(String userId);
}
