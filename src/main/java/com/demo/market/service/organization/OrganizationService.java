package com.demo.market.service.organization;

import com.demo.market.dto.organization.OrganizationRequest;
import com.demo.market.dto.organization.OrganizationResponse;

import java.util.Set;

public interface OrganizationService {

    Set<OrganizationResponse> getByUser(String userId);

    OrganizationResponse get(Long organizationId);

    Set<OrganizationResponse> getInactive();

    OrganizationResponse create(String userId, OrganizationRequest organizationRequest);

    OrganizationResponse activate(Long organizationId);

    OrganizationResponse delete(Long organizationId);
}
