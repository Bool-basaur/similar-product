package com.example.similar_product.adapter.out.http;

import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ProductHttpAdapter implements ProductPort {

    private final WebClient client;

    public ProductHttpAdapter(
            WebClient.Builder builder,
            @Value("${app.external.base-url}") String baseUrl
    ) {
        this.client = builder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Mono<List<String>> getSimilarProductIds(String productId) {
        return client.get()
                .uri("/product/{id}/similarids", productId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .onErrorResume(ex -> Mono.just(List.of()));
    }

    @Override
    public Mono<ProductDetail> getProductDetail(String productId) {
        return client.get()
                .uri("/product/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDetail.class)
                .onErrorResume(ex -> Mono.empty());
    }
}
