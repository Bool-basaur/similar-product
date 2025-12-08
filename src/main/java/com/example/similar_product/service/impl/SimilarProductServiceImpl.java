package com.example.similar_product.service.impl;



import com.example.similar_product.adapter.in.rest.exception.ProductNotFoundException;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import com.example.similar_product.service.SimilarProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class SimilarProductServiceImpl implements SimilarProductService {

    private final ProductPort api;
    private final int maxConcurrency;
    private final Duration detailTimeout;

    public SimilarProductServiceImpl(ProductPort api, int maxConcurrency, long timeoutMillis) {
        this.api = api;
        this.maxConcurrency = maxConcurrency;
        this.detailTimeout = Duration.ofMillis(timeoutMillis);
    }

    @Override
    public Mono<List<ProductDetail>> getSimilarProducts(String productId) {
        return api.getSimilarProductIds(productId)
                .flatMapMany(list -> Flux.fromIterable(list))
                .flatMap(simId ->
                                Mono.defer(() -> {
                                    Mono<ProductDetail> detailMono = api.getProductDetail(simId);
                                    if (detailMono == null) {
                                        return Mono.<ProductDetail>empty();
                                    }
                                    return detailMono
                                            .timeout(detailTimeout)
                                            .onErrorResume(e -> Mono.empty());
                                }),
                        maxConcurrency
                )
                .collectList();
    }
}