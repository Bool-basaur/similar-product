package com.example.similar_product.acceptance;

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
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = SimilarProductApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceParallelismTest {

    static MockWebServer mockServer;

    @LocalServerPort
    int port;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
        registry.add("app.external.base-url", () -> mockServer.url("/").toString());
        registry.add("app.service.parallelism", () -> "4");
    }

    @AfterAll
    static void shutdown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void parallelFetchIsFasterThanSequential() {

        mockServer.enqueue(new MockResponse()
                .setBody("[\"1\",\"2\",\"3\",\"4\"]")
                .addHeader("Content-Type","application/json"));

        for (int i = 0; i < 4; i++) {
            mockServer.enqueue(new MockResponse()
                    .setBody("{\"id\":\""+(i+1)+"\",\"name\":\"p\",\"price\":1.0,\"availability\":true}")
                    .setBodyDelay(600, TimeUnit.MILLISECONDS)
                    .addHeader("Content-Type", "application/json"));
        }

        long start = System.currentTimeMillis();

        WebClient client = WebClient.create("http://localhost:" + port);

        StepVerifier.create(
                        client.get().uri("/product/1/similar")
                                .retrieve()
                                .bodyToFlux(Object.class)
                )
                .expectNextCount(4)
                .verifyComplete();

        long duration = System.currentTimeMillis() - start;
        Assertions.assertTrue(duration < 1600, "Parallelism not working: " + duration);
    }
}