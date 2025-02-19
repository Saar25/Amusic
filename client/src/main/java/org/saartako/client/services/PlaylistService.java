package org.saartako.client.services;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.playlist.PlaylistDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlaylistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Gson GSON = new Gson();

    private final HttpService httpService;
    private final AuthService authService;

    private final ListProperty<Playlist> playlists = new SimpleListProperty<>(this, "playlists");

    private final ObjectProperty<Playlist> currentPlaylist = new SimpleObjectProperty<>(this, "currentPlaylist");

    private PlaylistService(HttpService httpService, AuthService authService) {
        this.httpService = httpService;
        this.authService = authService;

        // TODO: do it lazily
        this.authService.loggedUserProperty().addListener(observable -> fetchData());
        fetchData();
    }

    public static PlaylistService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ListProperty<Playlist> playlistsProperty() {
        return this.playlists;
    }

    public ObservableList<Playlist> getPlaylists() {
        return this.playlists.get();
    }

    public ObjectProperty<Playlist> currentPlaylistProperty() {
        return this.currentPlaylist;
    }

    public Playlist getCurrentPlaylist() {
        return this.currentPlaylist.get();
    }

    public void setCurrentPlaylist(Playlist playlist) {
        this.currentPlaylist.set(playlist);
    }

    public void fetchData() {
        fetchPlaylists().whenComplete((playlists, error) -> {
            if (playlists != null) {
                final ObservableList<Playlist> list =
                    FXCollections.observableArrayList(playlists);
                this.playlists.setValue(list);
            }
            if (error != null) {
                Platform.runLater(() -> {
                    final Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Failed to fetch playlists\n" + error.getMessage());
                    alert.show();
                });
            }
        });
    }

    public CompletableFuture<Playlist[]> fetchPlaylists() {
        final String jwtToken = this.authService.getJwtToken();
        if (jwtToken == null) {
            return CompletableFuture.completedFuture(null);
        }

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/playlist/mine"))
            .GET()
            .header("Authorization", "Bearer " + jwtToken)
            .build();

        LOGGER.info("Trying to fetch playlists");

        final CompletableFuture<HttpResponse<String>> send = this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return send.handle((response, error) -> {
            if (error != null) {
                LOGGER.info("Failed to fetch playlists - {}", error.getMessage());

                return null;
            } else if (response.statusCode() != 200) {
                LOGGER.info("Failed to fetch playlists - {}", response.body());

                return null;
            } else {
                LOGGER.info("Fetch playlists successfully");

                return GSON.fromJson(response.body(), PlaylistDTO[].class);
            }
        });
    }

    public CompletableFuture<Playlist> createPlaylist(CreatePlaylistDTO createPlaylist) {
        final String jwtToken = this.authService.getJwtToken();
        if (jwtToken == null) {
            return CompletableFuture.completedFuture(null);
        }

        final String payload = GSON.toJson(createPlaylist);

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/playlist"))
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .header("Authorization", "Bearer " + jwtToken)
            .header("Content-Type", "application/json")
            .build();

        LOGGER.info("Trying to create playlist");

        final CompletableFuture<HttpResponse<String>> send = this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return send.handle((response, error) -> {
            if (error != null) {
                LOGGER.info("Failed to create playlist - {}", error.getMessage());

                return null;
            } else if (response.statusCode() != 200) {
                LOGGER.info("Failed to create playlist - {}", response.body());

                return null;
            } else {
                LOGGER.info("Create playlist successfully");

                final Playlist playlist = GSON.fromJson(
                    response.body(), PlaylistDTO.class);
                this.playlists.add(playlist);
                return playlist;
            }
        });
    }

    public List<? extends Playlist> filterPlaylists(List<? extends Playlist> playlists, String filter) {
        final String lowercaseFilter = filter.toLowerCase();

        return filter.isEmpty() ? playlists : playlists.stream().filter(playlist ->
            playlist.getName().toLowerCase().contains(lowercaseFilter) ||
            (playlist.getOwner() != null && playlist.getOwner().getDisplayName().toLowerCase().contains(lowercaseFilter))
        ).toList();
    }

    public CompletableFuture<List<? extends Playlist>> filterPlaylistsAsync(List<? extends Playlist> playlists, String filter) {
        return CompletableFuture.supplyAsync(() -> filterPlaylists(playlists, filter));
    }

    private static final class InstanceHolder {
        private static final PlaylistService INSTANCE = new PlaylistService(
            HttpService.getInstance(),
            AuthService.getInstance());
    }
}
