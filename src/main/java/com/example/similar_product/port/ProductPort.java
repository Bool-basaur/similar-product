package com.example.similar_product.port;

import com.example.similar_product.domain.model.ProductDetail;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductPort {
    Mono<List<String>> getSimilarProductIds(String productId);
    Mono<ProductDetail> getProductDetail(String productId);
}
