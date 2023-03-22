package com.demo.market.service;

import com.demo.market.dto.auth.ChangePasswordForm;
import com.demo.market.dto.auth.KeycloakAuthResponse;
import com.demo.market.dto.auth.KeycloakUser;
import com.demo.market.dto.auth.LoginForm;
import com.demo.market.dto.auth.RegistrationDtoReq;
import com.demo.market.dto.auth.RoleMapping;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.HttpClientError;
import com.demo.market.exceptions.InvalidCredentials;
import com.demo.market.exceptions.ItemNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class KeycloakRequests {

    @Autowired
    RestTemplate restTemplate;

    @Value("${app.keycloak.login-url}")
    private String loginUrl;

    @Value("${app.keycloak.admin-url}")
    private String baseAdminUrl;

    @Value("${app.keycloak.root-id}")
    private String rootId;

    @Value("${app.keycloak.root-secret}")
    private String rootSecret;

    public String rootLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", rootId);
        requestBody.add("client_secret", rootSecret);
        requestBody.add("grant_type", "client_credentials");
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<KeycloakAuthResponse> response = restTemplate.exchange(loginUrl, HttpMethod.POST, formEntity, KeycloakAuthResponse.class);
        return response.getBody().getAccessToken();
    }

    public String login(LoginForm loginForm) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", loginForm.getUsername());
        requestBody.add("password", loginForm.getPassword());
        requestBody.add("client_id", "store_client");
        requestBody.add("grant_type", "password");
        requestBody.add("scope", "openid");
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    loginUrl,
                    HttpMethod.POST,
                    formEntity,
                    String.class);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new InvalidCredentials();
        }
    }

    public String getUserId(String username) {
        return getUser(username).getId();
    }

    public KeycloakUser getUser(String username) {
        String token = rootLogin();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<RegistrationDtoReq> userCreationRequest = new HttpEntity<>(headers);
        ResponseEntity<List<KeycloakUser>> response = restTemplate.exchange(
                baseAdminUrl + "/users?username={username}&exact=true",
                HttpMethod.GET,
                userCreationRequest,
                new ParameterizedTypeReference<>() {
                },
                username);
        return response.getBody().stream().findFirst().orElseThrow(() -> new ItemNotFound(Type.USER));
    }

    public List<RoleMapping> getUserRoles(String userId) {
        String token = rootLogin();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<RegistrationDtoReq> userCreationRequest = new HttpEntity<>(headers);
        ResponseEntity<List<RoleMapping>> response = restTemplate.exchange(
                baseAdminUrl + "/users/{userId}/role-mappings/realm/composite",
                HttpMethod.GET,
                userCreationRequest,
                new ParameterizedTypeReference<>() {
                }, userId);
        return response.getBody();
    }

    public void changePassword(RegistrationDtoReq registrationDtoReq, String userId) {
        String token = rootLogin();
        ChangePasswordForm changePasswordForm = new ChangePasswordForm(registrationDtoReq.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<ChangePasswordForm> passwordChangeRequest = new HttpEntity<>(changePasswordForm, headers);
        System.out.println(changePasswordForm);
        ResponseEntity<String> response = restTemplate.exchange(
                baseAdminUrl + "/users/{id}/reset-password",
                HttpMethod.PUT,
                passwordChangeRequest,
                String.class,
                userId);
        assert response.getStatusCode().is2xxSuccessful();
    }

    public void register(RegistrationDtoReq registrationDtoReq) {
        String token = rootLogin();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<RegistrationDtoReq> userCreationRequest = new HttpEntity<>(registrationDtoReq, headers);
        try {
            restTemplate.exchange(
                    baseAdminUrl + "/users",
                    HttpMethod.POST,
                    userCreationRequest,
                    String.class);
        } catch (HttpClientErrorException ex) {
            throw new HttpClientError(ex.getStatusCode(), ex.getMessage());
        }
    }

    public void update(KeycloakUser keycloakUser) {
        String token = rootLogin();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<KeycloakUser> userCreationRequest = new HttpEntity<>(keycloakUser, headers);
        try {
            restTemplate.exchange(
                    baseAdminUrl + "/users/{userId}",
                    HttpMethod.PUT,
                    userCreationRequest,
                    String.class,
                    keycloakUser.getId());
        } catch (HttpClientErrorException ex) {
            throw new HttpClientError(ex.getStatusCode(), ex.getMessage());
        }
    }

    public void delete(String userId) {
        String token = rootLogin();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> userCreationRequest = new HttpEntity<>("body", headers);
        try {
            restTemplate.exchange(
                    baseAdminUrl + "/users/{userId}",
                    HttpMethod.DELETE,
                    userCreationRequest,
                    String.class,
                    userId);
        } catch (HttpClientErrorException ex) {
            throw new HttpClientError(ex.getStatusCode(), ex.getMessage());
        }
    }
}
