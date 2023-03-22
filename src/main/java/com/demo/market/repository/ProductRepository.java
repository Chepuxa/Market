package com.demo.market.repository;

import com.demo.market.entity.Product;
import com.demo.market.enums.ActiveStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByIdAndStatus(Long id, ActiveStatus status);

    Set<Product> findAllByStatus(ActiveStatus status);

    Set<Product> findAllByStatusAndOrganizationId(ActiveStatus status, Long organizationId);
}
