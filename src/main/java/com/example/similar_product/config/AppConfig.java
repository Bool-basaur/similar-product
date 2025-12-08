package com.example.similar_product.config;

import com.example.similar_product.adapter.out.http.ProductHttpAdapter;
import com.example.similar_product.port.ProductPort;
import com.example.similar_product.service.SimilarProductService;
import com.example.similar_product.service.impl.SimilarProductServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ProductPort productPort(
            @Value("${app.external.base-url}") String baseUrl,
            @Value("${app.service.detail-timeout-ms}") long timeout
    ) {
        return new ProductHttpAdapter(baseUrl, timeout);
    }

    @Bean
    public SimilarProductService similarProductService(
            ProductPort productPort,
            @Value("${app.service.parallelism}") int parallelism,
            @Value("${app.service.detail-timeout-ms}") long timeout
    ) {
        return new SimilarProductServiceImpl(productPort, parallelism, timeout);
    }
}