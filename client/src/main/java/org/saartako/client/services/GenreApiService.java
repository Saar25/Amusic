package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.client.Config;
import org.saartako.client.utils.HttpUtils;
import org.saartako.common.genre.Genre;
import org.saartako.common.genre.GenreDTO;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class GenreApiService {

    private static final Gson GSON = new Gson();

    private final HttpService httpService;
    private final AuthService authService;

    private GenreApiService(HttpService httpService, AuthService authService) {
        this.httpService = httpService;
        this.authService = authService;
    }

    public static GenreApiService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public CompletableFuture<Genre[]> fetchGenres() {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/genre"))
                .GET()
                .header("Authorization", "Bearer " + authorization)
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> GSON.fromJson(response.body(), GenreDTO[].class));
        });
    }

    private static final class InstanceHolder {
        private static final GenreApiService INSTANCE = new GenreApiService(
            HttpService.getInstance(),
            AuthService.getInstance()
        );
    }
}
