package com.example.similar_product.domain.model;

/**
 * =============================================================================
 * @Class: ProductDetail
 * @Layer: Domain Model
 * @Description: Immutable record representing a product in the domain.
 *               Contains the main attributes of a product.
 * =============================================================================
 * @Author: Alex Jiménez Fernández
 * =============================================================================
 */
public record ProductDetail(String id, String name, double price, boolean availability) {}

