package com.demo.market.mappers;

import com.demo.market.dto.organization.OrganizationRequest;
import com.demo.market.dto.organization.OrganizationResponse;
import com.demo.market.entity.Organization;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Named("OrganizationMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProductMapper.class})
public interface OrganizationMapper {

    @Named("toOrganizationDtoSet")
    @IterableMapping(qualifiedByName = "toOrganizationDto")
    Set<OrganizationResponse> toDtoSet(Set<Organization> organizationEntitySet);

    @Named("toOrganizationDto")
    @Mappings({
            @Mapping(target = "products", qualifiedByName = {"ProductMapper", "toProductDto"})})
    OrganizationResponse toDto(Organization organization);

    @Named("toOrganizationDtoWithoutProducts")
    @Mappings({
            @Mapping(target = "products", ignore = true)})
    OrganizationResponse toDtoWithoutProducts(Organization organization);

    Organization toEntity(OrganizationRequest organizationRequest);
}