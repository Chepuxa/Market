package com.demo.market.repository;

import com.demo.market.entity.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    Set<Review> findByUserId(String userId);

    Set<Review> findByProductId(Long userId);
}
