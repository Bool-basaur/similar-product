package com.example.similar_product.contract;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OpenApiContractTest {

    @Test
    void openApiEndpointAvailable() throws Exception {
        URL url = new URL("http://localhost:5000/v3/api-docs");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        // Accept 200 or 404 (if not enabled)
        assertEquals(200, code);
    }
}
