package com.example.similar_product.adapter.in.rest.controller;

import com.example.similar_product.app.usecases.ValidateProductId;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.service.SimilarProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * =============================================================================
 * @Class: SimilarProductController
 * @Layer: Inbound Adapter (REST)
 * @Description: Exposes HTTP GET endpoint to query the similar products
 *               for a given product
 * =============================================================================
 * @Author Alex Jiménez Fernández
 **/

@RestController
@RequestMapping("/product")
public class SimilarProductController {

    private final SimilarProductService service;
    private final ValidateProductId validateProductId;

    public SimilarProductController(
            SimilarProductService service,
            ValidateProductId validateProductId
    ) {
        this.service = service;
        this.validateProductId = validateProductId;
    }

    /**
     *   Retrieves similar products based on product ID.
     *
     * @param productId the product id
     * @Return ResponseEntity of List<ProductDetail>. It returns the similar products list if they were found,
     *         with a 200 response. If similar products were not found, it returns a 404 response.
     **/

    @GetMapping("/{productId}/similar")
    public Mono<ResponseEntity<List<ProductDetail>>> getSimilar(@PathVariable String productId) {

        String validId = validateProductId.validate(productId);

        return service.getSimilarProducts(validId)
                .map(list ->
                        list.isEmpty()
                                ? ResponseEntity.noContent().build()
                                : ResponseEntity.ok(list)
                );
    }
}