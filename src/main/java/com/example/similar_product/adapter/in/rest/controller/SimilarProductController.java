package com.example.similar_product.adapter.in.rest.controller;

import com.example.similar_product.adapter.in.rest.exception.ProductNotFoundException;
import com.example.similar_product.adapter.in.rest.util.ProductIdNormalizer;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.service.SimilarProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
@RequestMapping("/product")
public class SimilarProductController {
    private final SimilarProductService service;

    private final ProductIdNormalizer normalizer;
    public SimilarProductController(SimilarProductService service, ProductIdNormalizer normalizer) {
        this.service = service;
        this.normalizer = normalizer;
    }

    @GetMapping("/{productId}/similar")
    public Mono<ResponseEntity<?>> getSimilar(
            @PathVariable String productId
    ) {
        String decoded = URLDecoder.decode(productId, StandardCharsets.UTF_8);
        String normalized = URLDecoder.decode(decoded, StandardCharsets.UTF_8).trim();

        if (normalized.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return service.getSimilarProducts(normalized)
                .flatMap(list ->
                        list.isEmpty()
                                ? Mono.just(ResponseEntity.noContent().build())
                                : Mono.just(ResponseEntity.ok(list)))
                .onErrorResume(ProductNotFoundException.class,
                        e -> Mono.just(ResponseEntity.notFound().build()));
    }

}
