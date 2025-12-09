package com.example.similar_product.unitary;

import com.example.similar_product.adapter.in.rest.controller.SimilarProductController;
import com.example.similar_product.app.usecases.ValidateProductId;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.service.SimilarProductService;

import com.example.similar_product.adapter.in.rest.exception.InvalidProductIdException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@WebFluxTest(SimilarProductController.class)
class SimilarProductControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private SimilarProductService service;

    @MockBean
    private ValidateProductId validateProductId;

    @Test
    void whenProductIdBlank_then400() {
        when(validateProductId.validate(anyString()))
                .thenThrow(new InvalidProductIdException("Invalid"));

        client.get()
                .uri("/product/%20/similar")
                .exchange()
                .expectStatus().isBadRequest();

        verify(service, never()).getSimilarProducts(anyString());
    }

    @Test
    void whenNoSimilar_then204() {
        when(validateProductId.validate("1"))
                .thenReturn("1");

        when(service.getSimilarProducts("1"))
                .thenReturn(Mono.just(List.of()));

        client.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void whenNotFound_then400() {
        when(validateProductId.validate(anyString()))
                .thenThrow(new InvalidProductIdException("Invalid"));

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

        when(validateProductId.validate("1")).thenReturn("1");
        when(service.getSimilarProducts("1")).thenReturn(Mono.just(list));

        client.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1");
    }
}
