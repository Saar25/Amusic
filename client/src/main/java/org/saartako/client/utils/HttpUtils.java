package org.saartako.client.utils;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class HttpUtils {

    private HttpUtils() {
    }

    public static <T> CompletableFuture<HttpResponse<T>> validateResponse(HttpResponse<T> response) {
        return switch (response.statusCode()) {
            case 200, 201, 202, 204 -> CompletableFuture.completedFuture(response);
            case 400 -> CompletableFuture.failedFuture(
                new Exception("Bad Request: " + response.body()));
            case 401 -> CompletableFuture.failedFuture(
                new Exception("Unauthorized: " + response.body()));
            case 403 -> CompletableFuture.failedFuture(
                new Exception("Forbidden: " + response.body()));
            case 404 -> CompletableFuture.failedFuture(
                new Exception("Not Found: " + response.body()));
            case 500 -> CompletableFuture.failedFuture(
                new Exception("Internal Server Error: " + response.body()));
            default -> CompletableFuture.failedFuture(
                new Exception("Unexpected HTTP status: " + response.statusCode() + " - " + response.body()));
        };
    }
}
