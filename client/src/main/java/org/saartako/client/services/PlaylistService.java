package org.saartako.client.services;

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
import org.saartako.common.song.Song;
import org.saartako.common.song.SongDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlaylistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PlaylistApiService playlistApiService;

    private final ListProperty<Playlist> playlists = new SimpleListProperty<>(this, "playlists");

    private final ObjectProperty<Playlist> currentPlaylist = new SimpleObjectProperty<>(this, "currentPlaylist");

    private PlaylistService(PlaylistApiService playlistApiService, AuthService authService) {
        this.playlistApiService = playlistApiService;

        // TODO: do it lazily
        authService.loggedUserProperty().addListener(observable -> fetchData());
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
        LOGGER.info("Trying to fetch playlists");

        return this.playlistApiService.fetchPlaylists()
            .whenComplete((playlists, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to fetch playlists - {}", throwable.getMessage());

                    this.playlists.setValue(FXCollections.emptyObservableList());
                } else {
                    LOGGER.info("Fetch playlists successfully");

                    final ObservableList<Playlist> list =
                        FXCollections.observableArrayList(playlists);
                    this.playlists.setValue(list);
                }
            });
    }

    public CompletableFuture<Playlist> createPlaylist(CreatePlaylistDTO createPlaylist) {
        LOGGER.info("Trying to create playlist");

        return this.playlistApiService.createPlaylist(createPlaylist)
            .whenComplete((playlist, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to create playlist - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to create playlist");

                    this.playlists.add(playlist);
                }
            });
    }

    public CompletableFuture<Playlist> deletePlaylist(Playlist playlist) {
        LOGGER.info("Trying to delete playlist");

        return this.playlistApiService.deletePlaylist(playlist.getId())
            .whenComplete((never, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to delete playlist - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to delete playlist");

                    this.playlists.remove(playlist);
                }
            });
    }

    public CompletableFuture<Void> addPlaylistSong(Playlist playlist, Song song) {
        LOGGER.info("Trying to add song to playlist");

        return this.playlistApiService.addPlaylistSong(playlist, song)
            .whenComplete((never, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to add song to playlist - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to add song to playlist");

                    int index = this.playlists.indexOf(playlist);
                    if (index == -1) {
                        LOGGER.warn("Playlist not found in local list, skipping update ({})", playlist.getId());
                    }

                    ((PlaylistDTO) playlist).getSongs().add((SongDTO) song);
                    this.playlists.set(index, playlist);
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
            PlaylistApiService.getInstance(),
            AuthService.getInstance()
        );
    }
}
