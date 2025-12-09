package com.example.similar_product.service.impl;

import com.example.similar_product.adapter.in.rest.exception.ProductNotFoundException;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import com.example.similar_product.service.SimilarProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * =============================================================================
 * @Class: SimilarProductServiceImpl
 * @Layer: Service Implementation
 * @Description: Implements the logic to fetch similar products
 * =============================================================================
 * @Author: Alex Jiménez Fernández
 * =============================================================================
 */
public class SimilarProductServiceImpl implements SimilarProductService {

    private static final Logger log = LoggerFactory.getLogger(SimilarProductServiceImpl.class);

    private final ProductPort api;
    private final int maxConcurrency;
    private final Duration detailTimeout;

    public SimilarProductServiceImpl(ProductPort api, int maxConcurrency, long timeoutMillis) {
        this.api = api;
        this.maxConcurrency = Math.max(1, maxConcurrency);
        this.detailTimeout = Duration.ofMillis(timeoutMillis);
    }

    @Override
    public Mono<List<ProductDetail>> getSimilarProducts(String productId) {

        log.info("[SERVICE] -> getSimilarProducts for {}", productId);

        return api.getSimilarProductIds(productId)
                .flatMapMany(list -> Flux.fromIterable(list))
                .flatMapSequential(simId ->
                                Mono.defer(() -> {
                                    log.debug("[SERVICE] Fetching detail for similar id {}", simId);
                                    return api.getProductDetail(simId)
                                            // Protect at service level as well if adapter didn't
                                            .timeout(detailTimeout)
                                            .onErrorResume(e -> {
                                                log.warn("[SERVICE] Skipping similar id {} due to error: {}", simId, e.toString());
                                                return Mono.empty();
                                            });
                                }),
                        maxConcurrency
                )
                .filter(detail -> detail != null)
                .collectList()
                .doOnSuccess(list -> log.info("[SERVICE] Completed fetching similar products for {} -> found {}", productId, list.size()))
                .onErrorResume(e -> {
                    if (e instanceof ProductNotFoundException) {
                        log.warn("[SERVICE] Primary product not found: {}", productId);
                        return Mono.error(e);
                    }
                    log.error("[SERVICE] Unexpected error when fetching similar products for {}: {}", productId, e.toString());
                    return Mono.just(List.of());
                });
    }
}
