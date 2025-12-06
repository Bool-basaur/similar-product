package com.example.similar_product.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class SimilarProductEndToEndTest {
    @Test
    void endToEnd() {
        WebClient client = WebClient.create("http://localhost:5000");

        StepVerifier.create(
                        client.get().uri("/product/1/similar")
                                .retrieve()
                                .bodyToFlux(Object.class)
                ).expectNextCount(1)
                .verifyComplete();
    }
}
