package org.saartako.client.services;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.saartako.common.song.CreateSongDTO;
import org.saartako.common.song.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SongService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SongApiService songApiService;

    private final ListProperty<Song> songs = new SimpleListProperty<>(this, "songs");

    private final LongProperty currentSongId = new SimpleLongProperty(this, "currentSongId");

    private final ObjectBinding<Song> currentSong = Bindings.createObjectBinding(() -> {
        final long songId = this.currentSongId.get();
        final Optional<Song> optional = this.songs.stream()
            .filter(p -> p.getId() == songId).findAny();
        return optional.orElse(null);
    }, this.songs, this.currentSongId);

    private SongService(SongApiService songApiService) {
        this.songApiService = songApiService;
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

    public ObjectBinding<Song> currentSongProperty() {
        return this.currentSong;
    }

    public Song getCurrentSong() {
        return this.currentSong.get();
    }

    public void setCurrentSong(Song song) {
        this.currentSongId.set(song.getId());
    }

    public void fetchData() {
        fetchSongs().whenComplete((songs, error) -> {
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
        LOGGER.info("Trying to fetch songs");

        return this.songApiService.fetchSongs()
            .whenComplete((songs, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to fetch songs - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Fetch songs successfully");

                    final ObservableList<Song> list =
                        FXCollections.observableArrayList(songs);
                    this.songs.setValue(list);
                }
            });
    }

    public CompletableFuture<Void> deleteSong(Song song) {
        LOGGER.info("Trying to delete song");

        return this.songApiService.deleteSong(song.getId())
            .whenComplete((never, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to delete song - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to delete song");

                    this.songs.remove(song);
                }
            });
    }

    public CompletableFuture<Song> createSong(CreateSongDTO createSong) {
        LOGGER.info("Trying to create song");

        return this.songApiService.createSong(createSong)
            .whenComplete((song, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to create song - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to create song");

                    this.songs.add(song);
                }
            });
    }

    public List<? extends Song> filterSongs(List<? extends Song> songs, String filter) {
        final String lowercaseFilter = filter.toLowerCase();

        return filter.isEmpty() ? songs : songs.stream().filter(song ->
            song.getName().toLowerCase().contains(lowercaseFilter) ||
            (song.getUploader() != null && song.getUploader().getDisplayName().toLowerCase().contains(lowercaseFilter)) ||
            (song.getGenre() != null && song.getGenre().getName().toLowerCase().contains(lowercaseFilter)) ||
            (song.getLanguage() != null && song.getLanguage().getName().toLowerCase().contains(lowercaseFilter))
        ).toList();
    }

    public CompletableFuture<List<? extends Song>> filterSongsAsync(List<? extends Song> songs, String filter) {
        return CompletableFuture.supplyAsync(() -> filterSongs(songs, filter));
    }

    private static final class InstanceHolder {
        private static final SongService INSTANCE = new SongService(
            SongApiService.getInstance()
        );
    }
}
