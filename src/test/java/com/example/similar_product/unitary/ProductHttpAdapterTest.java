package com.example.similar_product.unitary;
import com.example.similar_product.adapter.out.http.ProductHttpAdapter;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest(httpPort = 3001)
class ProductHttpAdapterTest {

    @Test
    void getProductDetailShouldReturnDetail() {
        stubFor(get(urlEqualTo("/product/2"))
                .willReturn(okJson("{\"id\":\"2\",\"name\":\"p2\",\"price\":12.5,\"availability\":true}")));

        ProductHttpAdapter adapter = new ProductHttpAdapter(WebClient.builder().baseUrl("http://localhost:3001"));

        StepVerifier.create(adapter.getProductDetail("2"))
                .expectNextMatches(p -> p.id().equals("2") && p.price() == 12.5)
                .verifyComplete();
    }

    @Test
    void getSimilarIdsShouldReturnList() {
        stubFor(get(urlEqualTo("/product/1/similarids"))
                .willReturn(okJson("[\"2\",\"3\"]")));

        ProductHttpAdapter adapter = new ProductHttpAdapter(WebClient.builder().baseUrl("http://localhost:3001"));

        StepVerifier.create(adapter.getSimilarProductIds("1"))
                .expectNextMatches(list -> list.size() == 2 && list.get(0).equals("2"))
                .verifyComplete();
    }
}