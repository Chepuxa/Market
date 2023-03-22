package com.demo.market.service.purchase;

import com.demo.market.dto.purchase.PurchaseResponse;
import com.demo.market.entity.Purchase;
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
    public Set<PurchaseResponse> getAll(String userId) {
        return userRepository.findById(userId)
                .map(usr -> purchaseMapper.toDtoSet(purchaseRepository.findByUserId(userId)))
                .orElseThrow(() -> new ItemNotFound(Type.USER));
    }

    @Override
    public PurchaseResponse get(Long purchaseId, String userId, Set<String> roles) {
        Purchase purchase;
        if (!roles.contains(Role.ADMIN.withPrefix())) {
            userRepository.findByIdAndStatus(userId, ActiveStatus.ACTIVE)
                    .orElseThrow(InsufficientRights::new);
            purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new ItemNotFound(Type.PURCHASE));
            if (!Objects.equals(purchase.getUser().getId(), userId)) {
                throw new InsufficientRights();
            }
        } else {
            purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new ItemNotFound(Type.PURCHASE));
        }
        return purchaseMapper.toDto(purchase);
    }

    @Override
    public PurchaseResponse refund(Long purchaseId, String userId, Set<String> roles) {
        Purchase purchase;
        if (!roles.contains(Role.ADMIN.withPrefix())) {
            userRepository.findByIdAndStatus(userId, ActiveStatus.ACTIVE)
                    .orElseThrow(InsufficientRights::new);
            purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new ItemNotFound(Type.PURCHASE));
            if (!Objects.equals(purchase.getUser().getId(), userId)) {
                throw new InsufficientRights();
            }
            if (Timestamp.from(Instant.now().minus(refundTime, ChronoUnit.HOURS)).after(purchase.getBuyTime())) {
                throw new RefundTimeExpired();
            }
        } else {
            purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new ItemNotFound(Type.PURCHASE));
        }
        PurchaseResponse response = purchaseMapper.toDto(purchase);
        purchaseRepository.delete(purchase);
        return response;
    }
}
