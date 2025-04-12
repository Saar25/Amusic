package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.client.Config;
import org.saartako.client.utils.HttpUtils;
import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.playlist.PlaylistDTO;
import org.saartako.common.song.Song;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Service for playlist api calls
 */
public class PlaylistApiService {

    private static final Gson GSON = new Gson();

    private final HttpService httpService;
    private final AuthService authService;

    private PlaylistApiService(HttpService httpService, AuthService authService) {
        this.httpService = httpService;
        this.authService = authService;
    }

    public static PlaylistApiService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public CompletableFuture<Playlist[]> fetchPlaylists() {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/playlist/mine"))
                .GET()
                .header("Authorization", "Bearer " + authorization)
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> GSON.fromJson(response.body(), PlaylistDTO[].class));
        });
    }

    public CompletableFuture<PlaylistDTO> createPlaylist(CreatePlaylistDTO createPlaylist) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final String payload = GSON.toJson(createPlaylist);

            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/playlist"))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .header("Authorization", "Bearer " + authorization)
                .header("Content-Type", "application/json")
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> GSON.fromJson(response.body(), PlaylistDTO.class));
        });
    }

    public CompletableFuture<Void> deletePlaylist(long playlistId) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/playlist/" + playlistId))
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

    public CompletableFuture<Void> addPlaylistSong(Playlist playlist, Song song) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/playlist/" + playlist.getId() + "/song/" + song.getId()))
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

    public CompletableFuture<Void> deletePlaylistSong(Playlist playlist, Song song) {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/playlist/" + playlist.getId() + "/song/" + song.getId()))
                .DELETE()
                .header("Authorization", "Bearer " + authorization)
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> null);
        });
    }

    private static final class InstanceHolder {
        private static final PlaylistApiService INSTANCE = new PlaylistApiService(
            HttpService.getInstance(),
            AuthService.getInstance()
        );
    }
}
