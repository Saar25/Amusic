package org.saartako.client.services;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import org.saartako.song.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SongService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static SongService INSTANCE;

    private final HttpService httpService;

    private final ObjectProperty<List<Song>> songsProperty = new SimpleObjectProperty<>(this, "songs", null);

    public SongService(HttpService httpService) {
        this.httpService = httpService;
    }

    public static synchronized SongService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SongService(HttpService.getInstance());
            INSTANCE.fetchData();
        }
        return INSTANCE;
    }

    public ObjectProperty<List<Song>> songsProperty() {
        return this.songsProperty;
    }

    public void fetchData() {
        fetchSongs().whenComplete((songs, error) -> {
            if (songs != null) {
                this.songsProperty.setValue(List.of(songs));
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
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Trying to fetch songs");
                final Song[] songs = this.httpService.fetchSongs();
                LOGGER.info("Fetch songs successfully");

                return songs;
            } catch (IOException | InterruptedException e) {
                LOGGER.info("Failed fetch songs - {}", e.getMessage());

                throw new RuntimeException(e);
            }
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
}
