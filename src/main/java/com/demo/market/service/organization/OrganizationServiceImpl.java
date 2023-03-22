package com.demo.market.service.organization;

import com.demo.market.dto.organization.OrganizationRequest;
import com.demo.market.dto.organization.OrganizationResponse;
import com.demo.market.entity.Organization;
import com.demo.market.entity.User;
import com.demo.market.enums.ActiveStatus;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.InsufficientRights;
import com.demo.market.exceptions.ItemNotFound;
import com.demo.market.exceptions.OrganizationAlreadyExists;
import com.demo.market.mappers.OrganizationMapper;
import com.demo.market.repository.OrganizationRepository;
import com.demo.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationMapper organizationMapper;

    @Override
    public Set<OrganizationResponse> getByUser(String userId) {
        User user = userRepository.findByIdAndStatus(userId, ActiveStatus.ACTIVE)
                .orElseThrow(InsufficientRights::new);
        Set<Organization> organizations = organizationRepository.findAllByUserId(user.getId());
        return organizationMapper.toDtoSet(organizations);
    }

    @Override
    public OrganizationResponse get(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .map(organizationMapper::toDto)
                .orElseThrow(() -> new ItemNotFound(Type.ORGANIZATION));
    }

    @Override
    public Set<OrganizationResponse> getInactive() {
        Set<Organization> organizationsOnHold = organizationRepository.findAllByStatus(ActiveStatus.ON_HOLD);
        return organizationMapper.toDtoSet(organizationsOnHold);
    }

    @Override
    public OrganizationResponse create(String userId, OrganizationRequest organizationRequest) {
        return userRepository.findById(userId)
                .map(usr -> {
                    Optional<Organization> existingOrganization = organizationRepository.findByName(organizationRequest.getName());
                    existingOrganization.ifPresent(org -> {
                        throw new OrganizationAlreadyExists();
                    });
                    Organization organization = organizationMapper.toEntity(organizationRequest);
                    organization.setStatus(ActiveStatus.ON_HOLD);
                    organization.setUser(usr);
                    return organizationMapper.toDto(organizationRepository.save(organization));
                })
                .orElseThrow(() -> new ItemNotFound(Type.USER));
    }

    @Override
    public OrganizationResponse activate(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .map(org -> {
                    if (org.getStatus().equals(ActiveStatus.ON_HOLD) || org.getStatus().equals(ActiveStatus.INACTIVE)) {
                        org.setStatus(ActiveStatus.ACTIVE);
                        org.getProducts().forEach(prd -> prd.setStatus(ActiveStatus.ACTIVE));
                    } else {
                        org.setStatus(ActiveStatus.INACTIVE);
                        org.getProducts().forEach(prd -> prd.setStatus(ActiveStatus.INACTIVE));
                    }
                    return organizationMapper.toDto(organizationRepository.save(org));
                })
                .orElseThrow(() -> new ItemNotFound(Type.ORGANIZATION));
    }

    @Override
    public OrganizationResponse delete(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .map(org -> {
                    OrganizationResponse response = organizationMapper.toDto(org);
                    organizationRepository.delete(org);
                    return response;
                })
                .orElseThrow(() -> new ItemNotFound(Type.ORGANIZATION));
    }
}
