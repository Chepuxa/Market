package com.demo.market.mappers;

import com.demo.market.dto.user.UserResponse;
import com.demo.market.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Named("UserMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse toDto(User user);
}