package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.client.Config;
import org.saartako.client.utils.HttpUtils;
import org.saartako.client.utils.MultipartFormData;
import org.saartako.common.song.CreateSongDTO;
import org.saartako.common.song.Song;
import org.saartako.common.song.SongDTO;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Service for song api calls
 */
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
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/song"))
                .GET()
                .header("Authorization", "Bearer " + authorization)
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> GSON.fromJson(response.body(), SongDTO[].class));
        });
    }

    public CompletableFuture<Void> deleteSong(long songId) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
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
        });
    }

    public CompletableFuture<SongDTO> uploadSong(CreateSongDTO createSong, File audioFile) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final String payload = GSON.toJson(createSong);
            final MultipartFormData multipartFormData;
            try {
                final MultipartFormData.Builder builder = MultipartFormData.builder();
                if (audioFile != null) {
                    builder.filePart("file", "application/octet-stream", audioFile);
                }
                multipartFormData = builder
                    .stringPart("song", "application/json", payload)
                    .build();
            } catch (IOException e) {
                return CompletableFuture.failedFuture(e);
            }

            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/song/upload"))
                .POST(multipartFormData.bodyPublisher())
                .header("Authorization", "Bearer " + authorization)
                .header("Content-Type", multipartFormData.contentType())
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> GSON.fromJson(response.body(), SongDTO.class));
        });
    }

    public CompletableFuture<Long[]> fetchLikedSongIds() {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
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
        });
    }

    public CompletableFuture<Void> likeSong(long songId) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/song/" + songId + "/like"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Authorization", "Bearer " + authorization)
                .header("Content-Type", "application/json")
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> null);
        });
    }

    public CompletableFuture<Long> fetchSongLikeCount(long songId) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/song/" + songId + "/like"))
                .GET()
                .header("Authorization", "Bearer " + authorization)
                .header("Content-Type", "application/json")
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> GSON.fromJson(response.body(), Long.class));
        });
    }

    public CompletableFuture<Void> unlikeSong(long songId) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/song/" + songId + "/like"))
                .DELETE()
                .header("Authorization", "Bearer " + authorization)
                .header("Content-Type", "application/json")
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> null);
        });
    }

    private static final class InstanceHolder {
        private static final SongApiService INSTANCE = new SongApiService(
            HttpService.getInstance(),
            AuthService.getInstance()
        );
    }
}
