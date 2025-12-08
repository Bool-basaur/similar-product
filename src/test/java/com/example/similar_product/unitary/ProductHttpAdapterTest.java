package com.example.similar_product.unitary;

import com.example.similar_product.adapter.out.http.ProductHttpAdapter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import reactor.test.StepVerifier;

import java.util.List;

public class ProductHttpAdapterTest {

    static MockWebServer server;

    @BeforeAll
    static void setup() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void stop() throws Exception {
        server.shutdown();
    }

    @Test
    void testSimilarIds() {
        server.enqueue(new MockResponse()
                .setBody("[\"2\",\"3\"]")
                .addHeader("Content-Type", "application/json"));

        ProductHttpAdapter adapter =
                new ProductHttpAdapter(server.url("/").toString(), 1500);

        StepVerifier.create(adapter.getSimilarProductIds("1"))
                .expectNext(List.of("2", "3"))
                .verifyComplete();
    }

    @Test
    void testDetailReturnsEmptyOn404() {
        server.enqueue(new MockResponse().setResponseCode(404));

        ProductHttpAdapter adapter =
                new ProductHttpAdapter(server.url("/").toString(), 1500);

        StepVerifier.create(adapter.getProductDetail("2"))
                .expectComplete()
                .verify();
    }
}