package com.example.similar_product.app.usecases;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.example.similar_product.adapter.in.rest.exception.InvalidProductIdException;
import org.springframework.stereotype.Component;

@Component
public class ValidateProductId {

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