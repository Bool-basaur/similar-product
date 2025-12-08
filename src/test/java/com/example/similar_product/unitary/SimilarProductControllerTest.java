package com.example.similar_product.unitary;

import com.example.similar_product.adapter.in.rest.controller.SimilarProductController;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.service.SimilarProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(SimilarProductController.class)
public class SimilarProductControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private SimilarProductService service;

    @Test
    void whenProductIdBlank_then400() {
        when(service.getSimilarProducts(anyString()))
                .thenReturn(Mono.just(List.of()));

        client.get()
                .uri("/product/%20/similar")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenNoSimilar_then204() {
        when(service.getSimilarProducts("1"))
                .thenReturn(Mono.just(List.of()));

        client.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void whenNotFound_then404() {
        client.get()
                .uri("/product/%20/similar")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenSimilarFound_then200() {
        List<ProductDetail> list = List.of(
                new ProductDetail("1", "A", 10.0, true)
        );

        when(service.getSimilarProducts("1"))
                .thenReturn(Mono.just(list));

        client.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1");
    }
}