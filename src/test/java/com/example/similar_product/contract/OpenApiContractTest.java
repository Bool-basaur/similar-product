package com.example.similar_product.contract;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OpenApiContractTest {

    @LocalServerPort
    int port;

    @Test
    void openApiEndpointAvailable() throws Exception {
        URL url = new URL("http://localhost:" + port + "/v3/api-docs");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        assertEquals(200, conn.getResponseCode());
    }
}
