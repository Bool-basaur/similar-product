package com.example.similar_product.unitary;

import com.example.similar_product.adapter.in.rest.exception.InvalidProductIdException;
import com.example.similar_product.app.usecases.ValidateProductId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateProductIdTest {

    ValidateProductId validator = new ValidateProductId();

    @Test
    void givenEncodedBlank_thenThrows() {
        assertThrows(InvalidProductIdException.class,
                () -> validator.validate("%20"));
    }

    @Test
    void givenValidId_thenReturnsNormalized() {
        String result = validator.validate("  123 ");
        assertEquals("123", result);
    }

    @Test
    void givenBadEncoding_thenFallsBackToOriginal() {
        String result = validator.validate("%E0%A4%A");
        assertEquals("%E0%A4%A", result);
    }
}