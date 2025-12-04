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
        service = new SimilarProductServiceImpl(api); // FIX
    }

    @Test
    void whenPrimaryNotFound_thenReturnEmpty() {
        Mockito.when(api.getSimilarProductIds("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.getSimilarProducts("1"))
                .verifyComplete();
    }

    @Test
    void whenSomeDetailsMissing_thenSkipThem() {
        Mockito.when(api.getSimilarProductIds("1"))
                .thenReturn(Mono.just(List.of("2","3")));

        Mockito.when(api.getProductDetail("2"))
                .thenReturn(Mono.just(new ProductDetail("2","p2",10,true)));

        Mockito.when(api.getProductDetail("3"))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.getSimilarProducts("1"))
                .expectNextMatches(list ->
                        list.size() == 1 && list.get(0).id().equals("2"))
                .verifyComplete();
    }
}
