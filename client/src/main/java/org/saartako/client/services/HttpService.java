package org.saartako.client.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    private HttpService() {
    }

    public static HttpService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public String login(String username, String password) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/auth/login?username=" + username + "&password=" + password))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (send.statusCode() != 200) {
            throw new IOException(send.body());
        }

        return send.body();
    }

    public String register(String username, String password, String displayName) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/auth/register?username=" + username + "&password=" + password + "&displayName=" + displayName))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (send.statusCode() != 200) {
            throw new IOException(send.body());
        }

        return send.body();
    }

    private static final class InstanceHolder {
        private static final HttpService INSTANCE = new HttpService();
    }
}
