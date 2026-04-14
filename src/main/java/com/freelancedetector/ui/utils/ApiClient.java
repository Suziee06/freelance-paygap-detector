package com.freelancedetector.ui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8081/api"; 
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static CompletableFuture<String> post(String endpoint, Object body) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonBody = mapper.writeValueAsString(body);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + endpoint))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    return response.body();
                } else {
                    throw new RuntimeException("API Error " + response.statusCode() + ": " + response.body());
                }
            } catch (Exception e) {
                throw new RuntimeException("Network Error: " + e.getMessage());
            }
        });
    }

    public static CompletableFuture<String> get(String endpoint) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + endpoint))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    return response.body();
                } else {
                    throw new RuntimeException("API Error " + response.statusCode() + ": " + response.body());
                }
            } catch (Exception e) {
                throw new RuntimeException("Network Error: " + e.getMessage());
            }
        });
    }
}
