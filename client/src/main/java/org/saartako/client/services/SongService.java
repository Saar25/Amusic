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
import org.saartako.client.utils.HttpUtils;
import org.saartako.common.song.Song;
import org.saartako.common.song.SongDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SongService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Gson GSON = new Gson();

    private final HttpService httpService;
    private final AuthService authService;

    private final ListProperty<Song> songs = new SimpleListProperty<>(this, "songs");

    private final ObjectProperty<Song> currentSong = new SimpleObjectProperty<>(this, "currentSong");

    private SongService(HttpService httpService, AuthService authService) {
        this.httpService = httpService;
        this.authService = authService;
        fetchData();
    }

    public static SongService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ListProperty<Song> songsProperty() {
        return this.songs;
    }

    public ObservableList<Song> getSongs() {
        return this.songs.get();
    }

    public ObjectProperty<Song> currentSongProperty() {
        return this.currentSong;
    }

    public Song getCurrentSong() {
        return this.currentSong.get();
    }

    public void setCurrentSong(Song song) {
        this.currentSong.set(song);
    }

    public void fetchData() {
        fetchSongs().whenComplete((songs, error) -> {
            if (songs != null) {
                final ObservableList<Song> list =
                    FXCollections.observableArrayList(songs);
                this.songs.setValue(list);
            }
            if (error != null) {
                Platform.runLater(() -> {
                    final Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Failed to fetch songs\n" + error.getMessage());
                    alert.show();
                });
            }
        });
    }

    public CompletableFuture<Song[]> fetchSongs() {
        if (!this.authService.isLoggedIn()) {
            return CompletableFuture.completedFuture(null);
        }

        final String authorization = this.authService.getJwtToken();

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/song"))
            .GET()
            .header("Authorization", "Bearer " + authorization)
            .build();

        LOGGER.info("Trying to fetch songs");

        return this.httpService.getHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenCompose(HttpUtils::validateResponse)
            .exceptionallyCompose(error -> {
                LOGGER.info("Failed to fetch songs - {}", error.getMessage());

                return CompletableFuture.failedFuture(error);
            })
            .thenApply(response -> {
                LOGGER.info("Fetch songs successfully");

                return GSON.fromJson(response.body(), SongDTO[].class);
            });
    }

    public List<? extends Song> filterSongs(List<? extends Song> songs, String filter) {
        final String lowercaseFilter = filter.toLowerCase();

        return filter.isEmpty() ? songs : songs.stream().filter(song ->
            song.getName().toLowerCase().contains(lowercaseFilter) ||
            (song.getUploader() != null && song.getUploader().getDisplayName().toLowerCase().contains(lowercaseFilter)) ||
            (song.getGenre() != null && song.getGenre().getName().toLowerCase().contains(lowercaseFilter))
        ).toList();
    }

    public CompletableFuture<List<? extends Song>> filterSongsAsync(List<? extends Song> songs, String filter) {
        return CompletableFuture.supplyAsync(() -> filterSongs(songs, filter));
    }

    private static final class InstanceHolder {
        private static final SongService INSTANCE = new SongService(
            HttpService.getInstance(),
            AuthService.getInstance());
    }
}
