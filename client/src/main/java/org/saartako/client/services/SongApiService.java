package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.client.Config;
import org.saartako.client.utils.HttpUtils;
import org.saartako.common.song.CreateSongDTO;
import org.saartako.common.song.Song;
import org.saartako.common.song.SongDTO;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class SongApiService {

    private static final Gson GSON = new Gson();

    private final HttpService httpService;
    private final AuthService authService;

    private SongApiService(HttpService httpService, AuthService authService) {
        this.httpService = httpService;
        this.authService = authService;
    }

    public static SongApiService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public CompletableFuture<Song[]> fetchSongs() {
        if (!this.authService.isLoggedIn()) {
            final Exception exception = new NullPointerException("User is not logged in");

            return CompletableFuture.failedFuture(exception);
        }

        final String authorization = this.authService.getJwtToken();

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(Config.serverUrl + "/song"))
            .GET()
            .header("Authorization", "Bearer " + authorization)
            .build();

        return this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenCompose(HttpUtils::validateResponse)
            .thenApply(response -> GSON.fromJson(response.body(), SongDTO[].class));
    }

    public CompletableFuture<Void> deleteSong(long songId) {
        if (!this.authService.isLoggedIn()) {
            final Exception exception = new NullPointerException("User is not logged in");

            return CompletableFuture.failedFuture(exception);
        }

        final String authorization = this.authService.getJwtToken();

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(Config.serverUrl + "/song/" + songId))
            .DELETE()
            .header("Authorization", "Bearer " + authorization)
            .header("Content-Type", "application/json")
            .build();

        return this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenCompose(HttpUtils::validateResponse)
            .thenApply(response -> null);
    }

    public CompletableFuture<Song> createSong(CreateSongDTO createSong) {
        if (!this.authService.isLoggedIn()) {
            final Exception exception = new NullPointerException("User is not logged in");

            return CompletableFuture.failedFuture(exception);
        }

        final String authorization = this.authService.getJwtToken();

        final String payload = GSON.toJson(createSong);

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(Config.serverUrl + "/song"))
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .header("Authorization", "Bearer " + authorization)
            .header("Content-Type", "application/json")
            .build();

        return this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenCompose(HttpUtils::validateResponse)
            .thenApply(response -> GSON.fromJson(response.body(), SongDTO.class));
    }

    public CompletableFuture<Long[]> fetchLikedSongIds() {
        if (!this.authService.isLoggedIn()) {
            final Exception exception = new NullPointerException("User is not logged in");

            return CompletableFuture.failedFuture(exception);
        }

        final String authorization = this.authService.getJwtToken();

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(Config.serverUrl + "/user/likes"))
            .GET()
            .header("Authorization", "Bearer " + authorization)
            .header("Content-Type", "application/json")
            .build();

        return this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenCompose(HttpUtils::validateResponse)
            .thenApply(response -> GSON.fromJson(response.body(), Long[].class));
    }

    private static final class InstanceHolder {
        private static final SongApiService INSTANCE = new SongApiService(
            HttpService.getInstance(),
            AuthService.getInstance()
        );
    }
}
