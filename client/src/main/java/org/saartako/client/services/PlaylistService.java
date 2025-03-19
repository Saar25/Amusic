package org.saartako.client.services;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.saartako.client.utils.BindingsUtils;
import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.playlist.PlaylistDTO;
import org.saartako.common.playlist.PlaylistUtils;
import org.saartako.common.song.Song;
import org.saartako.common.song.SongDTO;
import org.saartako.common.song.SongUtils;
import org.saartako.common.user.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlaylistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AtomicBoolean isUserDataValid = new AtomicBoolean(false);

    private final PlaylistApiService playlistApiService;
    private final SongService songService;
    private final AuthService authService;

    private final ListProperty<Playlist> fetchedPlaylists = new SimpleListProperty<>(this, "playlists");

    private final ObjectBinding<Playlist> likedSongsPlaylist;

    private final ListBinding<Playlist> allPlaylists;

    private final LongProperty currentPlaylistId = new SimpleLongProperty(this, "currentPlaylistId");

    private final ObjectBinding<Playlist> currentPlaylist;

    private PlaylistService(PlaylistApiService playlistApiService, SongService songService, AuthService authService) {
        this.playlistApiService = playlistApiService;
        this.songService = songService;
        this.authService = authService;

        this.likedSongsPlaylist = Bindings.createObjectBinding(() -> {
            return new PlaylistDTO()
                .setName("Liked Songs")
                .setModifiable(false)
                .setPrivate(true)
                .setOwner(UserUtils.copyDisplay(this.authService.getLoggedUser()))
                .setSongs(SongUtils.copyDisplay(this.songService.getLikedSongs()));
        }, this.authService.loggedUserProperty(), this.songService.likedSongsProperty());

        this.allPlaylists = BindingsUtils.createListBinding(() -> {
            final ObservableList<Playlist> fetchedPlaylists = PlaylistService.this.fetchedPlaylists.get();
            final Playlist likedSongsPlaylist = PlaylistService.this.likedSongsPlaylist.get();

            final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
            if (likedSongsPlaylist != null) playlists.addAll(likedSongsPlaylist);
            if (fetchedPlaylists != null) playlists.addAll(fetchedPlaylists);
            return playlists;
        }, this.fetchedPlaylists, this.likedSongsPlaylist);

        this.currentPlaylist = Bindings.createObjectBinding(() -> {
            final long playlistId = this.currentPlaylistId.get();
            final Optional<Playlist> optional = this.allPlaylists.stream()
                .filter(p -> p.getId() == playlistId).findAny();
            return optional.orElse(null);
        }, this.allPlaylists, this.currentPlaylistId);

        authService.loggedUserProperty().addListener(observable -> this.isUserDataValid.set(false));
    }

    public static PlaylistService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void fetchData() {
        if (this.isUserDataValid.compareAndSet(false, true)) {
            if (!this.authService.isLoggedIn()) {
                this.fetchedPlaylists.setValue(FXCollections.observableArrayList());
            } else {
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
        }
    }

    public ListBinding<Playlist> playlistsProperty() {
        return this.allPlaylists;
    }

    public ObservableList<Playlist> getPlaylists() {
        return this.allPlaylists.get();
    }

    public ObjectBinding<Playlist> currentPlaylistProperty() {
        return this.currentPlaylist;
    }

    public Playlist getCurrentPlaylist() {
        return this.currentPlaylist.get();
    }

    public void setCurrentPlaylist(Playlist playlist) {
        this.currentPlaylistId.set(playlist.getId());
    }

    public CompletableFuture<Playlist[]> fetchPlaylists() {
        LOGGER.info("Trying to fetch playlists");

        return this.playlistApiService.fetchPlaylists()
            .whenComplete((playlists, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to fetch playlists - {}", throwable.getMessage());

                    this.fetchedPlaylists.setValue(FXCollections.observableArrayList());
                } else {
                    LOGGER.info("Fetch playlists successfully");

                    this.fetchedPlaylists.setValue(FXCollections.observableArrayList(playlists));
                }
            });
    }

    public CompletableFuture<? extends Playlist> createPlaylist(CreatePlaylistDTO createPlaylist) {
        LOGGER.info("Trying to create playlist");

        return this.playlistApiService.createPlaylist(createPlaylist)
            .whenComplete((playlist, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to create playlist - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to create playlist");

                    playlist.setOwner(UserUtils.copyDisplay(this.authService.getLoggedUser()));
                    this.fetchedPlaylists.add(playlist);
                }
            });
    }

    public CompletableFuture<Void> deletePlaylist(Playlist playlist) {
        LOGGER.info("Trying to delete playlist");

        return this.playlistApiService.deletePlaylist(playlist.getId())
            .whenComplete((never, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to delete playlist - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to delete playlist");

                    this.fetchedPlaylists.remove(playlist);
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

                    int index = this.fetchedPlaylists.indexOf(playlist);
                    if (index == -1) {
                        LOGGER.warn("Playlist not found in local list, skipping update ({})", playlist.getId());
                    }

                    final PlaylistDTO newPlaylist = PlaylistUtils.copyDisplay(playlist);
                    newPlaylist.getSongs().add((SongDTO) song);
                    this.fetchedPlaylists.set(index, newPlaylist);
                }
            });
    }

    public CompletableFuture<Void> deletePlaylistSong(Playlist playlist, Song song) {
        LOGGER.info("Trying to delete song from playlist");

        return this.playlistApiService.deletePlaylistSong(playlist, song)
            .whenComplete((never, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to delete song from playlist - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to delete song from playlist");

                    int index = this.fetchedPlaylists.indexOf(playlist);
                    if (index == -1) {
                        LOGGER.warn("Playlist not found in local list, skipping update ({})", playlist.getId());
                    }

                    final PlaylistDTO newPlaylist = PlaylistUtils.copyDisplay(playlist);
                    newPlaylist.getSongs().removeIf(s -> s.getId() == song.getId());
                    this.fetchedPlaylists.set(index, newPlaylist);
                }
            });
    }

    public <T extends Playlist> List<T> filterPlaylists(List<T> playlists, String filter) {
        final String lowercaseFilter = filter.toLowerCase();

        return filter.isEmpty() ? playlists : playlists.stream().filter(playlist ->
            playlist.getName().toLowerCase().contains(lowercaseFilter) ||
            (playlist.getOwner() != null && playlist.getOwner().getDisplayName().toLowerCase().contains(lowercaseFilter))
        ).toList();
    }

    public <T extends Playlist> CompletableFuture<List<T>> filterPlaylistsAsync(List<T> playlists, String filter) {
        return CompletableFuture.supplyAsync(() -> filterPlaylists(playlists, filter));
    }

    private static final class InstanceHolder {
        private static final PlaylistService INSTANCE = new PlaylistService(
            PlaylistApiService.getInstance(),
            SongService.getInstance(),
            AuthService.getInstance());
    }
}
