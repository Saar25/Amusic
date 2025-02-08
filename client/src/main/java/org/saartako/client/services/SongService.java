package org.saartako.client.services;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private final HttpService httpService;

    private final ListProperty<Song> songsProperty = new SimpleListProperty<>(this, "songs");

    private SongService(HttpService httpService) {
        this.httpService = httpService;
        fetchData();
    }

    public static SongService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ListProperty<Song> songsProperty() {
        return this.songsProperty;
    }

    public void fetchData() {
        fetchSongs().whenComplete((songs, error) -> {
            if (songs != null) {
                final ObservableList<Song> list =
                    FXCollections.observableArrayList(songs);
                this.songsProperty.setValue(list);
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

    private static final class InstanceHolder {
        private static final SongService INSTANCE = new SongService(HttpService.getInstance());
    }
}
