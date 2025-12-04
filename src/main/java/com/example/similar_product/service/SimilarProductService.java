package com.example.similar_product.service;

import com.example.similar_product.domain.model.ProductDetail;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SimilarProductService {
    Mono<List<ProductDetail>> getSimilarProducts(String productId);
}
