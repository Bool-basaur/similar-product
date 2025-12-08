package com.example.similar_product.integration;


import com.example.similar_product.SimilarProductApplication;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;




@SpringBootTest(
        classes = SimilarProductApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SimilarProductIntegrationTest {

    @LocalServerPort
    int port;

    private static MockWebServer mockServer;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) throws Exception {
        if (mockServer == null) {
            mockServer = new MockWebServer();
            mockServer.start();
        }
        registry.add("app.external.base-url",
                () -> mockServer.url("/").toString());
    }

    @Autowired
    private WebTestClient client;

    @Test
    void fullFlow() {
        mockServer.enqueue(new MockResponse()
                .setBody("[\"2\",\"3\"]")
                .addHeader("Content-Type", "application/json"));

        mockServer.enqueue(new MockResponse()
                .setBody("{\"id\":\"2\",\"name\":\"p2\",\"price\":10,\"availability\":true}")
                .addHeader("Content-Type", "application/json"));

        mockServer.enqueue(new MockResponse()
                .setBody("{\"id\":\"3\",\"name\":\"p3\",\"price\":20,\"availability\":false}")
                .addHeader("Content-Type", "application/json"));

        client.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("2");
    }
}