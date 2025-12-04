package com.example.similar_product.adapter.in.web.controller;

import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.service.SimilarProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/product")
public class SimilarProductController {
    private final SimilarProductService service;

    public SimilarProductController(SimilarProductService service) {
        this.service = service;
    }

    @GetMapping("/{productId}/similar")
    public Mono<ResponseEntity<List<ProductDetail>>> getSimilar(@PathVariable String productId) {
        return service.getSimilarProducts(productId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
