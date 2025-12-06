package com.example.similar_product.resilience;

import com.example.similar_product.adapter.out.http.ProductHttpAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

@WireMockTest(httpPort = 3001)
public class SimilarProductResilienceTest {
    @Test
    void whenServiceReturns500_thenFallbackToEmpty() {
        stubFor(get(urlEqualTo("/product/1/similarids"))
                .willReturn(serverError()));

        ProductHttpAdapter adapter = new ProductHttpAdapter(WebClient.builder().baseUrl("http://localhost:3001"));

        StepVerifier.create(adapter.getSimilarProductIds("1"))
                .verifyComplete();
    }
}
