package org.saartako.client.services;

import org.saartako.client.utils.HttpUtils;
import org.saartako.common.encrypt.JwtParser;
import org.saartako.common.encrypt.UserJwtParser;
import org.saartako.common.user.User;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AuthApiService {

    private final HttpService httpService;

    public AuthApiService(HttpService httpService) {
        this.httpService = httpService;
    }

    public static AuthApiService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public CompletableFuture<String> login(String username, String password) {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/auth/login?username=" + username + "&password=" + password))
            .GET()
            .build();

        return this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenCompose(HttpUtils::validateResponse)
            .thenApply(HttpResponse::body);
    }

    public CompletableFuture<String> register(String username, String password, String displayName) {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/auth/register?" +
                            "username=" + username +
                            "&password=" + password +
                            "&displayName=" + displayName))
            .GET()
            .build();

        return this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenCompose(HttpUtils::validateResponse)
            .thenApply(HttpResponse::body);
    }

    private static final class InstanceHolder {
        private static final AuthApiService INSTANCE = new AuthApiService(HttpService.getInstance());
    }
}
