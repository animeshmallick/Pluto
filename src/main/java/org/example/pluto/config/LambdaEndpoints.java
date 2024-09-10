package org.example.pluto.config;

public enum LambdaEndpoints {
    GetProducts("https://lad6pt8032.execute-api.ap-south-1.amazonaws.com/items");

    private final String url;
    LambdaEndpoints(String url) {
        this.url = url;
    }

    public String getEndpoint() {
        return this.url;
    }
}
