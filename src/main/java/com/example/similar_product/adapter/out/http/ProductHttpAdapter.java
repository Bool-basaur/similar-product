package com.example.similar_product.adapter.out.http;

import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ProductHttpAdapter implements ProductPort {
    @Override
    public Mono<List<String>> getSimilarProductIds(String productId) {
        return null;
    }

    @Override
    public Mono<ProductDetail> getProductDetail(String productId) {
        return null;
    }
}
