package com.example.similar_product.adapter.in.rest.exception;

/**
 * =============================================================================
 * @Class: ProductNotFoundException
 * @Layer: Inbound Adapter (REST) - Exception
 * @Description: Creates the ProductNotFoundException error
 * =============================================================================
 * @Author Alex Jiménez Fernández
 **/

public class ProductNotFoundException extends RuntimeException {

    /**
     * Creates the message for the specific ProductNotFoundException
     **/
    public ProductNotFoundException() {
        super("No product found for the given parameters");
    }

    public ProductNotFoundException(String productId) {
        super("No product found for the given id: " + productId);
    }
}
