package com.demo.market.service.organization;

import com.demo.market.dto.Auth;
import com.demo.market.dto.organization.OrganizationRequest;
import com.demo.market.dto.organization.OrganizationResponse;

import java.util.Set;

public interface OrganizationService {

    Set<OrganizationResponse> getByUser(Auth auth);

    OrganizationResponse get(Auth auth, Long organizationId);

    Set<OrganizationResponse> getInactive();

    OrganizationResponse create(Auth auth, OrganizationRequest organizationRequest);

    OrganizationResponse activate(Long organizationId);

    OrganizationResponse delete(Long organizationId);
}
