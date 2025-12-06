package com.example.similar_product.adapter.out.http;

import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ProductHttpAdapter implements ProductPort {
    private final WebClient client;

    public ProductHttpAdapter(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://simulado:3001").build();
    }

    @CircuitBreaker(name = "productApi", fallbackMethod = "fallbackSimilarIds")
    @Retry(name = "productApi")
    @TimeLimiter(name = "productApi")
    @Override
    public Mono<List<String>> getSimilarProductIds(String productId) {
        return null;
    }

    private Mono<List<String>> fallbackSimilarIds(String productId, Throwable t) {
        return Mono.empty();
    }

    @Override
    @CircuitBreaker(name = "productApi", fallbackMethod = "fallbackProduct")
    @Retry(name = "productApi")
    @TimeLimiter(name = "productApi")
    public Mono<ProductDetail> getProductDetail(String productId) {
        return null;
    }

    private Mono<ProductDetail> fallbackProduct(String productId, Throwable t) {
        return Mono.empty();
    }
}
