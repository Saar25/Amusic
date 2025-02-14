package org.saartako.client.services;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.saartako.playlist.Playlist;
import org.saartako.playlist.PlaylistDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class PlaylistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Gson GSON = new Gson();

    private final HttpService httpService;
    private final AuthService authService;

    private final ListProperty<Playlist> playlists = new SimpleListProperty<>(this, "playlists");

    private PlaylistService(HttpService httpService, AuthService authService) {
        this.httpService = httpService;
        this.authService = authService;
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
        if (!this.authService.isLoggedIn()) {
            return CompletableFuture.completedFuture(null);
        }

        final String jwtToken = this.authService.getJwtToken();
        final long userId = this.authService.getLoggedUser().getId();

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/playlist?ownerId=" + userId))
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

    private static final class InstanceHolder {
        private static final PlaylistService INSTANCE = new PlaylistService(
            HttpService.getInstance(),
            AuthService.getInstance());
    }
}
