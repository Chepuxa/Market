package com.demo.market.mappers;

import com.demo.market.dto.purchase.PurchaseResponse;
import com.demo.market.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Named("PurchaseMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseMapper {

    Set<PurchaseResponse> toDtoSet(Set<Purchase> purchaseEntitySet);

    PurchaseResponse toDto(Purchase purchase);
}