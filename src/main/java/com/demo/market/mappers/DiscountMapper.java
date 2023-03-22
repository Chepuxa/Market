package com.demo.market.mappers;

import com.demo.market.dto.discount.DiscountDtoReq;
import com.demo.market.dto.discount.DiscountDtoResp;
import com.demo.market.entity.Discount;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Named("DiscountMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProductMapper.class})
public interface DiscountMapper {

    @Named("toDiscountDto")
    @Mappings({
            @Mapping(target = "products", qualifiedByName = {"ProductMapper", "toProductDto"})})
    DiscountDtoResp toDto(Discount discount);

    @Named("toDiscountDtoWithoutProducts")
    @Mappings({
            @Mapping(target = "products", ignore = true)})
    DiscountDtoResp toDtoWithoutProducts(Discount discount);

    @Named("toDiscountDtoSet")
    @IterableMapping(qualifiedByName = "toDiscountDto")
    Set<DiscountDtoResp> toDtoSet(Set<Discount> discountSet);

    Discount toEntity(DiscountDtoReq discountDtoReq);
}