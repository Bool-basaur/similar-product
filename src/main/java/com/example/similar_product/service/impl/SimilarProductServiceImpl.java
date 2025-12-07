package com.example.similar_product.service.impl;

import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import com.example.similar_product.service.SimilarProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class SimilarProductServiceImpl implements SimilarProductService {

    private final ProductPort productPort;
    private final int parallelism;
    private final Duration timeout;

    public SimilarProductServiceImpl(
            ProductPort productPort,
            @Value("${app.service.parallelism:4}") int parallelism,
            @Value("${app.service.product-detail-timeout-ms:2000}") long timeoutMs
    ) {
        this.productPort = productPort;
        this.parallelism = Math.max(parallelism, 1); // avoids flatMap(0)
        this.timeout = Duration.ofMillis(timeoutMs);
    }

    @Override
    public Mono<List<ProductDetail>> getSimilarProducts(String productId) {
        return productPort.getSimilarProductIds(productId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(
                        id -> productPort.getProductDetail(id)
                                .timeout(timeout)
                                .onErrorResume(e -> Mono.empty()),
                        parallelism
                )
                .collectList();
    }
}
