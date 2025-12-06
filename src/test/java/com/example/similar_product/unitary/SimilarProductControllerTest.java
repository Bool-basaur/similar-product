package com.example.similar_product.unitary;

import com.example.similar_product.adapter.in.web.controller.SimilarProductController;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.service.SimilarProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

class SimilarProductControllerTest {

    @Test
    void shouldReturn200() {
        SimilarProductService service = Mockito.mock(SimilarProductService.class);

        Mockito.when(service.getSimilarProducts("1"))
                .thenReturn(Mono.just(List.of(
                        new ProductDetail("2", "Prod2", 10, true)
                )));

        WebTestClient client = WebTestClient.bindToController(
                new SimilarProductController(service)
        ).build();

        client.get()
                .uri("/product/1/similar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("2");
    }
}