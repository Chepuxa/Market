package com.demo.market.repository;

import com.demo.market.entity.Organization;
import com.demo.market.enums.ActiveStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    Optional<Organization> findByName(String name);

    Set<Organization> findAllByUserId(String userId);

    Set<Organization> findAllByStatus(ActiveStatus status);

    Optional<Organization> findByIdAndStatus(Long organizationId, ActiveStatus status);
}
