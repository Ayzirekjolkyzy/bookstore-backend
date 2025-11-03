package com.okuylu_back.utils.exceptions;

import com.okuylu_back.utils.error_responses.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExpiredJwtErrorResponse> handleExpiredJwtException() {
        ExpiredJwtErrorResponse response = new ExpiredJwtErrorResponse("Your authentication token is expired, please re-login.", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<MalformedJwtErrorResponse> handleMalformedJwtException() {
        MalformedJwtErrorResponse response = new MalformedJwtErrorResponse("Your authentication token is invalid or malformed, please re-login.", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PersonException.class)
    private ResponseEntity<PersonErrorResponse> handlePersonException(PersonException e) {
        PersonErrorResponse response = new PersonErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartItemNotFoundException(CartItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<InsufficientStockErrorResponse> handleInsufficientStockException(InsufficientStockException ex) {
        InsufficientStockErrorResponse response = new InsufficientStockErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<PaymentErrorResponse> handlePaymentException(PaymentException ex) {
        PaymentErrorResponse response = new PaymentErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderProcessingException.class)
    public ResponseEntity<OrderProcessingErrorResponse> handleOrderProcessingException(OrderProcessingException ex) {
        OrderProcessingErrorResponse response = new OrderProcessingErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
