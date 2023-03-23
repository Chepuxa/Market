package com.demo.market.service.auth;

import com.demo.market.dto.auth.KeycloakUser;
import com.demo.market.dto.auth.LoginRequest;
import com.demo.market.dto.auth.RegistrationRequest;
import com.demo.market.dto.auth.RoleMapping;
import com.demo.market.entity.User;
import com.demo.market.enums.ActiveStatus;
import com.demo.market.enums.Role;
import com.demo.market.exceptions.InvalidCredentials;
import com.demo.market.exceptions.UserAlreadyExists;
import com.demo.market.exceptions.UserNotActive;
import com.demo.market.repository.UserRepository;
import com.demo.market.service.KeycloakRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KeycloakRequests keycloakRequests;

    @Value("${app.service.default-balance}")
    private Double balance;

    @Override
    public void register(RegistrationRequest registrationRequest) {
        Set<User> usersWithSameCredentials = userRepository.findAllByEmailOrUsername(registrationRequest.getEmail(), registrationRequest.getUsername());
        if (!usersWithSameCredentials.isEmpty()) {
            throw new UserAlreadyExists();
        }
        keycloakRequests.register(registrationRequest);
        String userId = keycloakRequests.getUserId(registrationRequest.getUsername());
        User user = new User();
        user.setId(userId);
        user.setEmail(registrationRequest.getEmail());
        user.setUsername(registrationRequest.getUsername());
        user.setBalance(balance);
        user.setStatus(ActiveStatus.ACTIVE);
        keycloakRequests.changePassword(registrationRequest, userId);
        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        KeycloakUser keycloakUser = keycloakRequests.getUser(loginRequest.getUsername());
        List<RoleMapping> roleMappings = keycloakRequests.getUserRoles(keycloakUser.getId());
        if (roleMappings.stream().noneMatch(role -> role.getName().equals(Role.ADMIN.withoutPrefix()))) {
            Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
            user.ifPresentOrElse(usr -> {
                if (!usr.getStatus().equals(ActiveStatus.ACTIVE)) {
                    throw new UserNotActive();
                }
            }, () -> {
                throw new InvalidCredentials();
            });
        }
        return keycloakRequests.login(loginRequest);
    }
}
