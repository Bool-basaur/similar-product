package com.example.similar_product.unitary;

import com.example.similar_product.adapter.out.http.ProductHttpAdapter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

public class ProductHttpAdapterTest {

    private MockWebServer server;
    private ProductHttpAdapter adapter;

    @BeforeEach
    void setup() throws Exception {
        server = new MockWebServer();
        server.start();

        adapter = new ProductHttpAdapter(
                WebClient.builder(),
                server.url("/").toString()
        );
    }

    @AfterEach
    void cleanup() throws Exception {
        server.shutdown();
    }

    @Test
    void getSimilarIdsShouldReturnList() {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("[\"2\", \"3\"]")
                        .addHeader("Content-Type", "application/json")
        );

        StepVerifier.create(adapter.getSimilarProductIds("1"))
                .expectNext(List.of("2", "3"))
                .verifyComplete();
    }
}