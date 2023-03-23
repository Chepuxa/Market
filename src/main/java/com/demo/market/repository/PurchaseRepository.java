package com.demo.market.repository;

import com.demo.market.entity.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

    Set<Purchase> findByUserId(String userId);

    Optional<Purchase> findByUserIdAndProductId(String userId, Long productId);
}
