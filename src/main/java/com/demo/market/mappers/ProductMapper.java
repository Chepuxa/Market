package com.demo.market.mappers;

import com.demo.market.dto.product.ProductRequest;
import com.demo.market.dto.product.ProductResponse;
import com.demo.market.entity.Product;
import com.demo.market.entity.Purchase;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Named("ProductMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DiscountMapper.class, OrganizationMapper.class, ReviewMapper.class})
public interface ProductMapper {

    Product toEntity(ProductRequest productRequest);

    @Named("toProductDtoSet")
    @IterableMapping(qualifiedByName = "toProductDto")
    Set<ProductResponse> toDtoSet(Set<Product> products);

    @Named("toProductDto")
    @Mappings({
            @Mapping(target = "discounts", qualifiedByName = {"DiscountMapper", "toDiscountDtoWithoutProducts"}),
            @Mapping(target = "organization", qualifiedByName = {"OrganizationMapper", "toOrganizationDtoWithoutProducts"}),
            @Mapping(target = "reviews", qualifiedByName = {"ReviewMapper", "toReviewDtoWithoutProduct"})})
    ProductResponse toDto(Product product);

    Purchase productToPurchase(Product product);
}