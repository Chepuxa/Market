package com.demo.market.service.discount;

import com.demo.market.dto.Auth;
import com.demo.market.dto.discount.DiscountRequest;
import com.demo.market.dto.discount.DiscountResponse;
import com.demo.market.entity.Discount;
import com.demo.market.entity.Product;
import com.demo.market.enums.ActiveStatus;
import com.demo.market.enums.Role;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.InsufficientRights;
import com.demo.market.exceptions.ItemNotFound;
import com.demo.market.mappers.DiscountMapper;
import com.demo.market.repository.DiscountRepository;
import com.demo.market.repository.ProductRepository;
import com.demo.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DiscountMapper discountMapper;

    @Override
    public DiscountResponse add(DiscountRequest discountRequest) {
        Discount discount = discountMapper.toEntity(discountRequest);
        Set<Product> products = new HashSet<>();
        discountRequest.getProductIds().forEach(id -> {
            Optional<Product> product = productRepository.findById(id);
            product.ifPresentOrElse(products::add, () -> {
                throw new ItemNotFound(Type.PRODUCT);
            });
        });
        discount.setProducts(products);
        return discountMapper.toDto(discountRepository.save(discount));
    }

    @Override
    public DiscountResponse update(Long discountId, DiscountRequest discountRequest) {
        Optional<Discount> discount = discountRepository.findById(discountId);
        return discount
                .map(dsc -> {
                    Set<Product> productSet = new HashSet<>();
                    discountRequest.getProductIds().forEach(id -> {
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
    public DiscountResponse get(Auth auth, Long discountId) {
        if (!auth.getUserRoles().contains(Role.ADMIN.withPrefix())) {
            userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE).orElseThrow(InsufficientRights::new);
        }
        Optional<Discount> discount = discountRepository.findById(discountId);
        return discount
                .map(discountMapper::toDto)
                .orElseThrow(() -> new ItemNotFound(Type.DISCOUNT));
    }

    @Override
    public DiscountResponse delete(Long discountId) {
        Optional<Discount> discount = discountRepository.findById(discountId);
        return discount
                .map(dsc -> {
                    discountRepository.delete(dsc);
                    return discountMapper.toDto(dsc);
                })
                .orElseThrow(() -> new ItemNotFound(Type.DISCOUNT));
    }
}
