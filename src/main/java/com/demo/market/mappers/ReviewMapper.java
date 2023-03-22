package com.demo.market.mappers;

import com.demo.market.dto.review.ReviewRequest;
import com.demo.market.dto.review.ReviewResponse;
import com.demo.market.entity.Review;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Named("ReviewMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProductMapper.class})
public interface ReviewMapper {

    Review toEntity(ReviewRequest reviewRequest);

    @Named("toReviewDto")
    @Mappings({
            @Mapping(target = "product", qualifiedByName = {"ProductMapper", "toProductDto"})})
    ReviewResponse toDto(Review review);

    @Named("toReviewDtoWithoutProduct")
    @Mappings({
            @Mapping(target = "product", ignore = true)})
    ReviewResponse toDtoWithoutProduct(Review review);

    @Named("toReviewDtoSet")
    @IterableMapping(qualifiedByName = "toReviewDto")
    Set<ReviewResponse> toDtoSet(Set<Review> reviewEntitySet);
}