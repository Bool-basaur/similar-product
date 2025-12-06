package com.example.similar_product.service.impl;

import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import com.example.similar_product.service.SimilarProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SimilarProductServiceImpl implements SimilarProductService {

    private static final Logger log = LoggerFactory.getLogger(SimilarProductServiceImpl.class);

    @Value("${app.service.parallelism:4}")
    private int parallelism;

    @Value("${app.service.product-detail-timeout-ms:2000}")
    private long timeout;

    private final ProductPort productPort;

    public SimilarProductServiceImpl(ProductPort productPort) {
        this.productPort = productPort;
    }

    @Override
    public Mono<List<ProductDetail>> getSimilarProducts(String productId) {
        return null;
    }
}
