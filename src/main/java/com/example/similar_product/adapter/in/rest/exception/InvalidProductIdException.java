package com.example.similar_product.adapter.in.rest.exception;

/**
 * =============================================================================
 * @Class: InvalidProductIdException
 * @Layer: Inbound Adapter (REST) - Exception
 * @Description: Creates the InvalidProductIdException error
 * =============================================================================
 * @Author Alex Jiménez Fernández
 **/

public class InvalidProductIdException extends RuntimeException {

    /**
     * Creates the message for the specific InvalidProductIdException
     **/
    public InvalidProductIdException(String message) {
        super(message);
    }
}