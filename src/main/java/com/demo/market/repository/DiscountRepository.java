package com.demo.market.repository;

import com.demo.market.entity.Discount;
import org.springframework.data.repository.CrudRepository;

public interface DiscountRepository extends CrudRepository<Discount, Long> {
}
