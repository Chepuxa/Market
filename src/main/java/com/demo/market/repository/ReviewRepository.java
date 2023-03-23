package com.demo.market.repository;

import com.demo.market.entity.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    Set<Review> findByUserId(String userId);

    Optional<Review> findByUserIdAndProductId(String userId, Long productId);
}
