package com.example.similar_product.adapter.in.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * =============================================================================
 * @Class: GlobalExceptionHandler
 * @Layer: Inbound Adapter (REST) - Exception
 * @Description: Manages different types of errors in the request response
 * =============================================================================
 * @Author Alex Jiménez Fernández
 **/

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "product_not_found",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<?> handleBadRequest(InvalidProductIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "bad_request",
                "message", ex.getMessage()
        ));
    }
}