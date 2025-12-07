package com.example.similar_product.resilience;


import com.example.similar_product.adapter.out.http.ProductHttpAdapter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.List;

public class SimilarProductResilienceTest {

    private MockWebServer mockServer;
    private ProductHttpAdapter adapter;

    @BeforeEach
    void setup() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("/").toString();

        adapter = new ProductHttpAdapter(
                WebClient.builder(),
                baseUrl
        );
    }

    @AfterEach
    void teardown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void whenBackendReturns500_thenFallbackToEmptyList() {
        mockServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setBody("server error")
        );

        StepVerifier.create(adapter.getSimilarProductIds("1"))
                .expectNext(List.of())
                .verifyComplete();
    }
}