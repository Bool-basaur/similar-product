package com.example.similar_product.app.usecases;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.example.similar_product.adapter.in.rest.exception.InvalidProductIdException;
import org.springframework.stereotype.Component;

/**
 * =============================================================================
 * @Class: ValidateProductId
 * @Layer: Use Case
 * @Description: Validates, decodes and sanitizes product identifiers coming
 *               from the REST layer before passing them into the domain/service.
 *               Ensures the productId is not null, not blank, and safe to use.
 * =============================================================================
 * @Author: Alex Jiménez Fernández
 * =============================================================================
 */
@Component
public class ValidateProductId {

    /**
     * Validates and normalizes the incoming productId.
     *
     * @param raw raw productId from HTTP path
     * @return valid productId
     * @throws InvalidProductIdException if the ID is null or blank
     */
    public String validate(String raw) {

        if (raw == null) {
            throw new InvalidProductIdException("productId cannot be null");
        }

        String decoded;
        try {
            decoded = URLDecoder.decode(raw, StandardCharsets.UTF_8);
        } catch (Exception e) {
            decoded = raw;
        }

        String normalized = decoded.trim();

        if (normalized.isBlank()) {
            throw new InvalidProductIdException("productId cannot be blank");
        }

        return normalized;
    }
}