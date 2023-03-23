package com.demo.market.service.review;

import com.demo.market.dto.Auth;
import com.demo.market.dto.review.ReviewRequest;
import com.demo.market.dto.review.ReviewResponse;

import java.util.Set;

public interface ReviewService {

    Set<ReviewResponse> getAll(Auth auth);

    ReviewResponse add(Auth auth, Long productId, ReviewRequest reviewRequest);

    ReviewResponse get(Auth auth, Long reviewId);

    Set<ReviewResponse> getAllByUser(String userId);

    ReviewResponse update(Auth auth, Long reviewId, ReviewRequest reviewRequest);

    ReviewResponse delete(Auth auth, Long reviewId);
}
