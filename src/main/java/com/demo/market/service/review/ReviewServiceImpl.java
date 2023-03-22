package com.demo.market.service.review;

import com.demo.market.dto.review.ReviewRequest;
import com.demo.market.dto.review.ReviewResponse;
import com.demo.market.entity.Purchase;
import com.demo.market.entity.Review;
import com.demo.market.entity.User;
import com.demo.market.enums.ActiveStatus;
import com.demo.market.enums.Role;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.AlreadyHasReview;
import com.demo.market.exceptions.InsufficientRights;
import com.demo.market.exceptions.ItemNotFound;
import com.demo.market.exceptions.ProductNotOwned;
import com.demo.market.mappers.ReviewMapper;
import com.demo.market.repository.ProductRepository;
import com.demo.market.repository.PurchaseRepository;
import com.demo.market.repository.ReviewRepository;
import com.demo.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    ReviewMapper reviewMapper;

    @Override
    public Set<ReviewResponse> getAll(String userId) {
        User user = userRepository.findByIdAndStatus(userId, ActiveStatus.ACTIVE)
                .orElseThrow(InsufficientRights::new);
        return reviewMapper.toDtoSet(reviewRepository.findByUserId(userId));
    }

    @Override
    public ReviewResponse add(Long productId, String userId, ReviewRequest reviewRequest) {
        User user = userRepository.findByIdAndStatus(userId, ActiveStatus.ACTIVE)
                .orElseThrow(InsufficientRights::new);
        return productRepository.findById(productId)
                .map(prd -> {
                    Set<Purchase> purchases = purchaseRepository.findByProductId(prd.getId());
                    if (purchases.stream().noneMatch(purchase -> purchase.getUser().getId().equals(userId))) {
                        throw new ProductNotOwned();
                    }
                    if (prd.getReviews().stream().anyMatch(review -> review.getUser().getId().equals(userId))) {
                        throw new AlreadyHasReview();
                    }
                    Review review = reviewMapper.toEntity(reviewRequest);
                    review.setUser(user);
                    review.setProduct(prd);
                    return reviewMapper.toDto(reviewRepository.save(review));
                })
                .orElseThrow(() -> new ItemNotFound(Type.PRODUCT));
    }

    @Override
    public ReviewResponse get(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(reviewMapper::toDto)
                .orElseThrow(() -> new ItemNotFound(Type.REVIEW));
    }

    @Override
    public Set<ReviewResponse> getAllWithUserId(String userId) {
        return userRepository.findById(userId)
                .map(usr -> reviewMapper.toDtoSet(reviewRepository.findByUserId(userId)))
                .orElseThrow(() -> new ItemNotFound(Type.USER));
    }

    @Override
    public Set<ReviewResponse> getAllWithProductId(Long productId) {
        return productRepository.findByIdAndStatus(productId, ActiveStatus.ACTIVE)
                .map(prd -> reviewMapper.toDtoSet(reviewRepository.findByProductId(productId)))
                .orElseThrow(() -> new ItemNotFound(Type.PRODUCT));
    }

    @Override
    public ReviewResponse update(String userId, Set<String> userRoles, Long reviewId, ReviewRequest reviewRequest) {
        Review review;
        if (!userRoles.contains(Role.ADMIN.withPrefix())) {
            userRepository.findByIdAndStatus(userId, ActiveStatus.ACTIVE)
                    .orElseThrow(InsufficientRights::new);
            review = reviewRepository.findById(reviewId).orElseThrow(() -> new ItemNotFound(Type.REVIEW));
            if (!Objects.equals(review.getUser().getId(), userId)) {
                throw new InsufficientRights();
            }
        } else {
            review = reviewRepository.findById(reviewId).orElseThrow(() -> new ItemNotFound(Type.REVIEW));
        }
        Review newReview = reviewMapper.toEntity(reviewRequest);
        newReview.setProduct(review.getProduct());
        newReview.setUser(review.getUser());
        return reviewMapper.toDto(reviewRepository.save(newReview));
    }

    @Override
    public ReviewResponse delete(String userId, Set<String> userRoles, Long reviewId) {
        Review review;
        if (!userRoles.contains(Role.ADMIN.withPrefix())) {
            userRepository.findByIdAndStatus(userId, ActiveStatus.ACTIVE)
                    .orElseThrow(InsufficientRights::new);
            review = reviewRepository.findById(reviewId).orElseThrow(() -> new ItemNotFound(Type.REVIEW));
            if (!Objects.equals(review.getUser().getId(), userId)) {
                throw new InsufficientRights();
            }
        } else {
            review = reviewRepository.findById(reviewId).orElseThrow(() -> new ItemNotFound(Type.REVIEW));
        }
        ReviewResponse response = reviewMapper.toDto(review);
        reviewRepository.delete(review);
        return response;
    }
}
