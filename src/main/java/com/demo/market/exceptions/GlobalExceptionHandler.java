package com.demo.market.exceptions;

import lombok.NonNull;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(Error.builder()
                        .message("Could not parse JSON")
                        .code((long) status.value())
                        .build());
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(Error.builder()
                        .message("Validation of one of the fields failed")
                        .code((long) status.value())
                        .build());
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatus status,
                                                        @NonNull WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(Error.builder()
                        .message("Type mismatch")
                        .code((long) status.value())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<Error> invalidCredentialsHandler(InvalidCredentials ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(HttpClientError.class)
    public ResponseEntity<Error> httpClientErrorHandler(HttpClientError ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) ex.getStatus().value())
                .message(ex.getMessage())
                .build(), ex.getStatus());
    }

    @ResponseBody
    @ExceptionHandler(NotEnoughCredits.class)
    public ResponseEntity<Error> notEnoughCreditHandler(NotEnoughCredits ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(ItemNotFound.class)
    public ResponseEntity<Error> itemNotFoundHandler(ItemNotFound ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(OutdatedDiscount.class)
    public ResponseEntity<Error> outdatedDiscountHandler(OutdatedDiscount ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(InsufficientRights.class)
    public ResponseEntity<Error> insufficientRightsHandler(InsufficientRights ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler(RefundTimeExpired.class)
    public ResponseEntity<Error> refundTimeExpiredHandler(RefundTimeExpired ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(AlreadyHasReview.class)
    public ResponseEntity<Error> alreadyHasReviewHandler(AlreadyHasReview ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(ProductNotOwned.class)
    public ResponseEntity<Error> productNotOwnedHandler(ProductNotOwned ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<Error> userAlreadyExistsHandler(UserAlreadyExists ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(UserNotActive.class)
    public ResponseEntity<Error> userNotActiveHandler(UserNotActive ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler(OrganizationAlreadyExists.class)
    public ResponseEntity<Error> organizationAlreadyExistsHandler(OrganizationAlreadyExists ex) {
        return new ResponseEntity<>(Error.builder()
                .code((long) HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build(), HttpStatus.CONFLICT);
    }
}
