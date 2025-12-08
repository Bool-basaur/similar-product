package com.example.similar_product.unitary;

import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import com.example.similar_product.service.SimilarProductService;
import com.example.similar_product.service.impl.SimilarProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class SimilarProductServiceTest {

    ProductPort api;
    SimilarProductService service;

    @BeforeEach
    void setup() {
        api = Mockito.mock(ProductPort.class);

        service = new SimilarProductServiceImpl(api, 4, 2000);
    }

    @Test
    void whenPrimaryNotFound_thenReturnEmpty() {

        Mockito.when(api.getSimilarProductIds("1"))
                .thenReturn(Mono.just(List.of()));

        StepVerifier.create(service.getSimilarProducts("1"))
                .expectNextMatches(list -> list.isEmpty())
                .verifyComplete();
    }

    @Test
    void whenSomeDetailsMissing_thenSkipThem() {
        Mockito.when(api.getSimilarProductIds("1"))
                .thenReturn(Mono.just(List.of("2", "3")));

        Mockito.when(api.getProductDetail("2"))
                .thenReturn(Mono.just(new ProductDetail("2", "p2", 10, true)));


        Mockito.when(api.getProductDetail("3"))
                .thenReturn(Mono.error(new RuntimeException("Boom")));

        StepVerifier.create(service.getSimilarProducts("1"))
                .expectNextMatches(list ->
                        list.size() == 1 &&
                                list.get(0).id().equals("2")
                )
                .verifyComplete();
    }

    @Test
    void whenAllDetailsOk_thenReturnAll() {
        Mockito.when(api.getSimilarProductIds("1"))
                .thenReturn(Mono.just(List.of("2","3")));

        Mockito.when(api.getProductDetail("2"))
                .thenReturn(Mono.just(new ProductDetail("2","A",10,true)));

        Mockito.when(api.getProductDetail("3"))
                .thenReturn(Mono.just(new ProductDetail("3","B",20,false)));

        StepVerifier.create(service.getSimilarProducts("1"))
                .expectNextMatches(list ->
                        list.size() == 2 &&
                                list.get(0).id().equals("2") &&
                                list.get(1).id().equals("3")
                )
                .verifyComplete();
    }

    @Test
    void whenDetailTimeout_thenSkipIt() {
        Mockito.when(api.getSimilarProductIds("1"))
                .thenReturn(Mono.just(List.of("2")));


        Mockito.when(api.getProductDetail("2"))
                .thenReturn(Mono.never());

        StepVerifier.create(service.getSimilarProducts("1"))
                .expectNextMatches(list -> list.isEmpty())
                .verifyComplete();
    }
}