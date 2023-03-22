package com.demo.market.repository;

import com.demo.market.entity.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

    Set<Purchase> findByUserId(String userId);

    Set<Purchase> findByProductId(Long productId);
}
