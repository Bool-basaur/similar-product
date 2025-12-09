package com.example.similar_product.port;

import com.example.similar_product.domain.model.ProductDetail;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * =============================================================================
 * @Interface: ProductPort
 * @Layer: Port
 * @Description: Defines the contract for obtaining product information from
 *               external systems
 * =============================================================================
 * @Author: Alex Jiménez Fernández
 * =============================================================================
 */
public interface ProductPort {
    /**
     * Fetches the IDs of similar products.
     *
     * @param productId product identifier
     * @return list of similar product IDs
     */
    Mono<List<String>> getSimilarProductIds(String productId);

    /**
     * Fetches detailed product information.
     *
     * @param productId product identifier
     * @return product detail, or empty if not found
     */
    Mono<ProductDetail> getProductDetail(String productId);
}
