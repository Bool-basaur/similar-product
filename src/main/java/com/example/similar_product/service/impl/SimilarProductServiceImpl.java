package com.example.similar_product.service.impl;

import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import com.example.similar_product.service.SimilarProductService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SimilarProductServiceImpl implements SimilarProductService {
    private final ProductPort productPort;

    public SimilarProductServiceImpl(ProductPort productPort) {
        this.productPort = productPort;
    }
    @Override
    public Mono<List<ProductDetail>> getSimilarProducts(String productId) {
        return null;
    }
}
