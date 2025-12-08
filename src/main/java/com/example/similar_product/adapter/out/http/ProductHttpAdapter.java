package com.example.similar_product.adapter.out.http;


import com.example.similar_product.adapter.in.rest.exception.ProductNotFoundException;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class ProductHttpAdapter implements ProductPort {

    private final WebClient client;
    private final Duration timeout;

    private static final ParameterizedTypeReference<List<String>> TYPE_LIST_STRING =
            new ParameterizedTypeReference<>() {};

    public ProductHttpAdapter(String baseUrl, long timeoutMillis) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.timeout = Duration.ofMillis(timeoutMillis);
    }

    @Override
    public Mono<List<String>> getSimilarProductIds(String productId) {
        return client.get()
                .uri("/product/{productId}/similarids", productId)
                .exchangeToMono(response -> {
                    if (response.statusCode().value() == 404) {
                        return Mono.error(new ProductNotFoundException("primary:" + productId));
                    }
                    return response.bodyToMono(TYPE_LIST_STRING);
                })
                .timeout(timeout)
                .onErrorResume(e -> {
                    if (e instanceof ProductNotFoundException) {
                        return Mono.error(e);
                    }
                    return Mono.just(List.of());
                });
    }

    @Override
    public Mono<ProductDetail> getProductDetail(String productId) {
        return client.get()
                .uri("/product/{productId}", productId)
                .exchangeToMono(response -> {
                    if (response.statusCode().value() == 404) {
                        return Mono.empty();
                    }
                    return response.bodyToMono(ProductDetail.class);
                })
                .timeout(timeout)
                .onErrorResume(e -> Mono.empty());
    }
}