package com.demo.market.service.discount;

import com.demo.market.dto.discount.DiscountDtoReq;
import com.demo.market.dto.discount.DiscountDtoResp;
import com.demo.market.entity.Discount;
import com.demo.market.entity.Product;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.ItemNotFound;
import com.demo.market.mappers.DiscountMapper;
import com.demo.market.repository.DiscountRepository;
import com.demo.market.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DiscountMapper discountMapper;

    @Override
    public DiscountDtoResp add(DiscountDtoReq discountDtoReq) {
        Discount discount = discountMapper.toEntity(discountDtoReq);
        Set<Product> products = new HashSet<>();
        discountDtoReq.getProductIds().forEach(id -> {
            Optional<Product> product = productRepository.findById(id);
            product.ifPresentOrElse(products::add, () -> {
                throw new ItemNotFound(Type.PRODUCT);
            });
        });
        discount.setProducts(products);
        return discountMapper.toDto(discountRepository.save(discount));
    }

    @Override
    public DiscountDtoResp update(Long discountId, DiscountDtoReq discountDtoReq) {
        Optional<Discount> discount = discountRepository.findById(discountId);
        return discount
                .map(dsc -> {
                    Set<Product> productSet = new HashSet<>();
                    discountDtoReq.getProductIds().forEach(id -> {
                        Optional<Product> product = productRepository.findById(id);
                        product.ifPresentOrElse(productSet::add, () -> {
                            throw new ItemNotFound(Type.PRODUCT);
                        });
                    });
                    dsc.setProducts(productSet);
                    discountRepository.save(dsc);
                    return discountMapper.toDto(dsc);
                })
                .orElseThrow(() -> new ItemNotFound(Type.DISCOUNT));
    }

    @Override
    public DiscountDtoResp get(Long discountId) {
        Optional<Discount> discount = discountRepository.findById(discountId);
        return discount
                .map(discountMapper::toDto)
                .orElseThrow(() -> new ItemNotFound(Type.DISCOUNT));
    }

    @Override
    public DiscountDtoResp delete(Long discountId) {
        Optional<Discount> discount = discountRepository.findById(discountId);
        return discount
                .map(dsc -> {
                    discountRepository.delete(dsc);
                    return discountMapper.toDto(dsc);
                })
                .orElseThrow(() -> new ItemNotFound(Type.DISCOUNT));
    }
}
