package org.saartako.client.utils;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
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

    public static HttpRequest.BodyPublisher bodyPublisherOfMultipartFormData(Path filePath, String boundary) {
        final String fileName = filePath.getFileName().toString();

        final String multipartHeader =
            "--" + boundary + "\r\n"
            + "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n"
            + "Content-Type: audio/mpeg\r\n\r\n";
        final byte[] headerBytes = multipartHeader.getBytes();

        final String multipartFooter = "\r\n--" + boundary + "--\r\n";
        final byte[] footerBytes = multipartFooter.getBytes();

        final HttpRequest.BodyPublisher filePublisher = HttpRequest.BodyPublishers.ofInputStream(() -> {
            try {
                return Files.newInputStream(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return HttpRequest.BodyPublishers.concat(
            HttpRequest.BodyPublishers.ofByteArray(headerBytes),
            filePublisher,
            HttpRequest.BodyPublishers.ofByteArray(footerBytes)
        );
    }

    public static String generateBoundary() {
        int length = 16;
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('A' + random.nextInt(26)));
        }
        return sb.toString();
    }
}
