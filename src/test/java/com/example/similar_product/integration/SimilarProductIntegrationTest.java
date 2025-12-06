package com.example.similar_product.integration;

import com.example.similar_product.SimilarProductApplication;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(classes = SimilarProductApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 3001)
class SimilarProductIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    void fullFlow() {

        stubFor(get(urlEqualTo("/product/1/similarids"))
                .willReturn(okJson("[\"2\",\"3\"]")));

        stubFor(get(urlEqualTo("/product/2"))
                .willReturn(okJson("{\"id\":\"2\",\"name\":\"p2\",\"price\":10.0,\"availability\":true}")));

        stubFor(get(urlEqualTo("/product/3"))
                .willReturn(okJson("{\"id\":\"3\",\"name\":\"p3\",\"price\":20.0,\"availability\":false}")));

        client.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id")
                .isEqualTo("2");
    }
}