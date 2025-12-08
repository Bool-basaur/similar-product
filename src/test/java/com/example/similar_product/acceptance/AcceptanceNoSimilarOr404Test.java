package com.example.similar_product.acceptance;

import com.example.similar_product.SimilarProductApplication;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
        classes = SimilarProductApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class AcceptanceNoSimilarOr404Test {

    static MockWebServer mockServer;

    @LocalServerPort
    int port;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
        registry.add("app.external.base-url", () -> mockServer.url("/").toString());
    }

    @AfterAll
    static void shutdown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void whenExternalReturns404_thenControllerReturns404() {
        mockServer.enqueue(new MockResponse().setResponseCode(404));

        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        client.get()
                .uri("/product/999/similar")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenNoSimilars_thenControllerReturns204() {
        mockServer.enqueue(new MockResponse()
                .setBody("[]")
                .addHeader("Content-Type", "application/json"));

        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        client.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isNoContent();
    }
}