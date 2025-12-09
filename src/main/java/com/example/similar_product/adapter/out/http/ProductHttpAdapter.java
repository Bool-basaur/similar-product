package com.example.similar_product.adapter.out.http;

import com.example.similar_product.adapter.in.rest.exception.ProductNotFoundException;
import com.example.similar_product.domain.model.ProductDetail;
import com.example.similar_product.port.ProductPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * =============================================================================
 * @Class: ProductHttpAdapter
 * @Layer: Outbound Adapter (REST)
 * @Description: Connects to the external Product API using WebClient.
 *  *               Fetches similar product IDs and detailed information for a
 *  *               product. Provides default error-handling and timeouts.
 * =============================================================================
 * @Author Alex Jiménez Fernández
 **/
public class ProductHttpAdapter implements ProductPort {

    private static final Logger log = LoggerFactory.getLogger(ProductHttpAdapter.class);

    private final WebClient client;
    private final Duration timeout;

    private static final String CB_NAME = "productApi";

    private static final ParameterizedTypeReference<List<String>> TYPE_LIST_STRING =
            new ParameterizedTypeReference<>() {};

    public ProductHttpAdapter(String baseUrl, long timeoutMillis) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.timeout = Duration.ofMillis(timeoutMillis);
    }




    /**
     * Retrieves a list of similar product IDs from the external API.
     *
     * @param productId product identifier
     * @return list of strings representing similar product IDs
     */
    @Override
    @CircuitBreaker(name = CB_NAME)
    @Retry(name = CB_NAME)
    public Mono<List<String>> getSimilarProductIds(String productId) {

        log.info("[ADAPTER] -> Fetching similar IDs for product {}", productId);

        return client.get()
                .uri("/product/{productId}/similarids", productId)
                .exchangeToMono(response -> {

                    if (response.statusCode().value() == 404) {
                        log.warn("[ADAPTER] Similar IDs not found for {}", productId);
                        return Mono.error(new ProductNotFoundException(productId));
                    }

                    return response.bodyToMono(TYPE_LIST_STRING);
                })
                .timeout(timeout)
                .doOnError(e -> log.error("[ADAPTER] Error fetching similar IDs for {}: {}", productId, e.toString()))
                .onErrorResume(e -> {
                    if (e instanceof ProductNotFoundException) {
                        return Mono.error(e);
                    }
                    log.warn("[ADAPTER] Fallback -> returning empty list of similar IDs for {} : {}", productId, e.toString());
                    return Mono.just(List.of());
                });
    }


    /**
     * Retrieves the detail of one product from the external API.
     *
     * @param productId product identifier
     * @return Mono of ProductDetail, empty on 404
     */
    @Override
    @CircuitBreaker(name = CB_NAME)
    @Retry(name = CB_NAME)
    public Mono<ProductDetail> getProductDetail(String productId) {

        log.debug("[ADAPTER] -> Fetching detail for {}", productId);

        return client.get()
                .uri("/product/{productId}", productId)
                .exchangeToMono(response -> {

                    if (response.statusCode().value() == 404) {
                        log.info("[ADAPTER] Detail not found for {}", productId);
                        return Mono.empty();
                    }

                    return response.bodyToMono(ProductDetail.class);
                })
                .timeout(timeout)
                .doOnError(e -> log.warn("[ADAPTER] Error fetching detail for {}: {}", productId, e.toString()))
                .onErrorResume(e -> {
                    log.warn("[ADAPTER] Fallback -> skipping detail for {}: {}", productId, e.toString());
                    return Mono.empty();
                });
    }
}
