package com.demo.market.service.purchase;

import com.demo.market.dto.Auth;
import com.demo.market.dto.purchase.PurchaseResponse;
import com.demo.market.entity.Purchase;
import com.demo.market.entity.User;
import com.demo.market.enums.ActiveStatus;
import com.demo.market.enums.Role;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.InsufficientRights;
import com.demo.market.exceptions.ItemNotFound;
import com.demo.market.exceptions.RefundTimeExpired;
import com.demo.market.mappers.PurchaseMapper;
import com.demo.market.repository.ProductRepository;
import com.demo.market.repository.PurchaseRepository;
import com.demo.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PurchaseMapper purchaseMapper;

    @Value("${app.service.refund-hours}")
    private Long refundTime;

    @Override
    public Set<PurchaseResponse> getAll(Auth auth) {
        return userRepository.findById(auth.getUserId())
                .map(usr -> purchaseMapper.toDtoSet(purchaseRepository.findByUserId(auth.getUserId())))
                .orElseThrow(InsufficientRights::new);
    }

    @Override
    public Set<PurchaseResponse> getAllByUser(String userId) {
        return purchaseMapper.toDtoSet(purchaseRepository.findByUserId(userId));
    }

    @Override
    public PurchaseResponse get(Auth auth, Long purchaseId) {
        Purchase purchase;
        if (!auth.getUserRoles().contains(Role.ADMIN.withPrefix())) {
            userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE)
                    .orElseThrow(InsufficientRights::new);
            purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new ItemNotFound(Type.PURCHASE));
            if (!Objects.equals(purchase.getUser().getId(), auth.getUserId())) {
                throw new InsufficientRights();
            }
        } else {
            purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new ItemNotFound(Type.PURCHASE));
        }
        return purchaseMapper.toDto(purchase);
    }

    @Override
    public PurchaseResponse refund(Auth auth, Long purchaseId) {
        Purchase purchase;
        if (!auth.getUserRoles().contains(Role.ADMIN.withPrefix())) {
            User user = userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE)
                    .orElseThrow(InsufficientRights::new);
            purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new ItemNotFound(Type.PURCHASE));
            if (!Objects.equals(purchase.getUser().getId(), auth.getUserId())) {
                throw new InsufficientRights();
            }
            if (Timestamp.from(Instant.now().minus(refundTime, ChronoUnit.HOURS)).after(purchase.getBuyTime())) {
                throw new RefundTimeExpired();
            }
            user.setBalance(user.getBalance() + purchase.getPriceWithDiscount());
            userRepository.save(user);
            productRepository.findById(purchase.getProductId())
                    .ifPresent(prd -> {
                        prd.setAmount(prd.getAmount() + 1);
                        productRepository.save(prd);
                    });
        } else {
            purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new ItemNotFound(Type.PURCHASE));
        }
        PurchaseResponse response = purchaseMapper.toDto(purchase);
        purchaseRepository.delete(purchase);
        return response;
    }
}
