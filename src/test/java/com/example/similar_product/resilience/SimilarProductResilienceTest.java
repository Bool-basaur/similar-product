package com.example.similar_product.resilience;

import com.example.similar_product.port.ProductPort;
import com.example.similar_product.service.SimilarProductService;
import com.example.similar_product.service.impl.SimilarProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class SimilarProductResilienceTest {

    @Test
    void timeoutSkipsProduct() {
        ProductPort port = Mockito.mock(ProductPort.class);

        Mockito.when(port.getSimilarProductIds("1"))
                .thenReturn(Mono.just(List.of("2")));

        Mockito.when(port.getProductDetail("2"))
                .thenReturn(Mono.never());

        SimilarProductService service =
                new SimilarProductServiceImpl(port, 3, 200);

        StepVerifier.create(service.getSimilarProducts("1"))
                .expectNext(List.of())
                .verifyComplete();
    }

    @Test
    void errorSkipsProduct() {
        ProductPort port = Mockito.mock(ProductPort.class);

        Mockito.when(port.getSimilarProductIds("1"))
                .thenReturn(Mono.just(List.of("2")));

        Mockito.when(port.getProductDetail("2"))
                .thenReturn(Mono.error(new RuntimeException("boom")));

        SimilarProductService service =
                new SimilarProductServiceImpl(port, 3, 200);

        StepVerifier.create(service.getSimilarProducts("1"))
                .expectNext(List.of())
                .verifyComplete();
    }
}