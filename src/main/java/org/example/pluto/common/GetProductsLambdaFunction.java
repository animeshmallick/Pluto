package org.example.pluto.common;

import org.example.pluto.config.LambdaEndpoints;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetProductsLambdaFunction {
    private final String getProductLambdaEndPoint;

    public GetProductsLambdaFunction(){
        this.getProductLambdaEndPoint = LambdaEndpoints.GetProducts.getEndpoint();
    }

    public String getAllProducts(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest api_request = HttpRequest.newBuilder()
                .uri(URI.create(this.getProductLambdaEndPoint))
                .build();

        HttpResponse<String> api_response;
        try {
            api_response = client.send(api_request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        return api_response.body();
    }
}
