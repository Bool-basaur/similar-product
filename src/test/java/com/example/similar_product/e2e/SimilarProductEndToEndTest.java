package com.example.similar_product.e2e;

import com.example.similar_product.SimilarProductApplication;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@SpringBootTest(
        classes = SimilarProductApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimilarProductEndToEndTest {

    static MockWebServer mockServer;

    @LocalServerPort
    int port;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) throws Exception {
        if (mockServer == null) {
            mockServer = new MockWebServer();
            mockServer.start();
        }
        registry.add("app.external.base-url", () -> mockServer.url("/").toString());
    }

    @AfterAll
    static void shutdown() throws Exception {
        if (mockServer != null) {
            mockServer.shutdown();
        }
    }

    @Test
    void endToEnd() {
        mockServer.enqueue(new MockResponse()
                .setBody("[\"2\"]")
                .addHeader("Content-Type", "application/json"));

        mockServer.enqueue(new MockResponse()
                .setBody("{\"id\":\"2\",\"name\":\"p2\",\"price\":10.0,\"availability\":true}")
                .addHeader("Content-Type", "application/json"));

        WebClient client = WebClient.create("http://localhost:" + port);

        StepVerifier.create(
                        client.get().uri("/product/1/similar")
                                .retrieve()
                                .bodyToFlux(Object.class)
                )
                .expectNextCount(1)
                .verifyComplete();
    }
}