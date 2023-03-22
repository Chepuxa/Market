package com.demo.market.service.review;

import com.demo.market.dto.review.ReviewRequest;
import com.demo.market.dto.review.ReviewResponse;

import java.util.Set;

public interface ReviewService {

    Set<ReviewResponse> getAll(String userId);
    ReviewResponse add(Long productId, String userId, ReviewRequest reviewRequest);
    ReviewResponse get(Long reviewId);
    Set<ReviewResponse> getAllWithUserId(String userId);
    Set<ReviewResponse> getAllWithProductId(Long productId);
    ReviewResponse update(String userId, Set<String> userRoles, Long reviewId, ReviewRequest reviewRequest);
    ReviewResponse delete(String userId, Set<String> userRoles, Long reviewId);
}
