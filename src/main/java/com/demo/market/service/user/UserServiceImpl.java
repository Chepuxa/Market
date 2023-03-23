package com.demo.market.service.user;

import com.demo.market.dto.Auth;
import com.demo.market.dto.auth.KeycloakUser;
import com.demo.market.dto.user.UpdateBalanceRequest;
import com.demo.market.dto.user.UserResponse;
import com.demo.market.enums.ActiveStatus;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.InsufficientRights;
import com.demo.market.exceptions.ItemNotFound;
import com.demo.market.mappers.UserMapper;
import com.demo.market.repository.UserRepository;
import com.demo.market.service.KeycloakRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KeycloakRequests keycloakRequests;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserResponse get(String userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ItemNotFound(Type.USER));
    }

    @Override
    public UserResponse getByUser(Auth auth) {
        return userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE)
                .map(userMapper::toDto)
                .orElseThrow(InsufficientRights::new);
    }

    @Override
    public UserResponse updateBalance(String userId, UpdateBalanceRequest updateBalanceRequest) {
        return userRepository.findById(userId)
                .map(usr -> {
                    usr.setBalance(updateBalanceRequest.getBalance());
                    return userMapper.toDto(userRepository.save(usr));
                })
                .orElseThrow(() -> new ItemNotFound(Type.USER));
    }

    @Override
    public UserResponse delete(String userId) {
        return userRepository.findById(userId)
                .map(usr -> {
                    UserResponse response = userMapper.toDto(usr);
                    userRepository.deleteById(userId);
                    keycloakRequests.delete(userId);
                    return response;
                })
                .orElseThrow(() -> new ItemNotFound(Type.USER));
    }

    @Override
    public UserResponse changeActiveStatus(String userId) {
        return userRepository.findById(userId)
                .map(usr -> {
                    boolean enabled = false;
                    if (usr.getStatus().equals(ActiveStatus.ACTIVE)) {
                        usr.setStatus(ActiveStatus.INACTIVE);
                    } else {
                        usr.setStatus(ActiveStatus.ACTIVE);
                        enabled = true;
                    }
                    KeycloakUser keycloakUser = new KeycloakUser();
                    keycloakUser.setId(userId);
                    keycloakUser.setEnabled(enabled);
                    keycloakRequests.update(keycloakUser);
                    return userMapper.toDto(userRepository.save(usr));
                })
                .orElseThrow(() -> new ItemNotFound(Type.USER));
    }
}
