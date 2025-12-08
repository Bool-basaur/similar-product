package com.example.similar_product.contract;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimilarProductsContractBehaviorTest {

    static MockWebServer mockServer;

    @LocalServerPort
    int port;

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
        registry.add("app.external.base-url",
                () -> mockServer.url("/").toString());
    }

    @AfterAll
    static void shutdown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void whenProductExists_returnsArray() {
        mockServer.enqueue(new MockResponse().setBody("[\"2\",\"3\"]").setHeader("Content-Type", "application/json"));
        mockServer.enqueue(new MockResponse().setBody("""
                {"id":"2","name":"Product 2","price":10.0,"availability":true}
                """).setHeader("Content-Type", "application/json"));
        mockServer.enqueue(new MockResponse().setBody("""
                {"id":"3","name":"Product 3","price":20.0,"availability":false}
                """).setHeader("Content-Type", "application/json"));

        WebClient client = WebClient.create("http://localhost:" + port);

        List<?> response = client.get()
                .uri("/product/1/similar")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .block();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void whenProductDoesNotExist_returns404() {
        mockServer.enqueue(new MockResponse().setResponseCode(404));

        WebClient client = WebClient.create("http://localhost:" + port);

        int status = client.get()
                .uri("/product/999/similar")
                .exchangeToMono(res -> {
                    return res.bodyToMono(Void.class).thenReturn(res.statusCode().value());
                })
                .block();

        assertEquals(404, status);
    }
}