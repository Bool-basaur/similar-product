package com.example.similar_product.service;

import com.example.similar_product.domain.model.ProductDetail;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * =============================================================================
 * @Interface: SimilarProductService
 * @Layer: Domain / Service
 * @Description: Defines the business operation of retrieving similar products.
 *               Implemented by SimilarProductServiceImpl.
 * =============================================================================
 * @Author: Alex Jiménez Fernández
 * =============================================================================
 */
public interface SimilarProductService {

    /**
     * Retrieves a list of ProductDetail for all similar products of a given ID.
     *
     * @param productId validated productId
     * @return list of product details
     */
    Mono<List<ProductDetail>> getSimilarProducts(String productId);
}
