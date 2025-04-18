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
import org.saartako.common.song.CreateSongDTO;
import org.saartako.common.song.Song;
import org.saartako.common.user.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service for song state management
 */
public class SongService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SongApiService songApiService;
    private final AuthService authService;

    private final AtomicBoolean isDataValid = new AtomicBoolean(false);
    private final AtomicBoolean isUserDataValid = new AtomicBoolean(false);
    private final Map<Long, AtomicLong> songsLikeCount = new ConcurrentHashMap<>();

    private final ListProperty<Song> songs = new SimpleListProperty<>(this, "songs");

    private final LongProperty currentSongId = new SimpleLongProperty(this, "currentSongId");

    private final ObjectBinding<Song> currentSong = Bindings.createObjectBinding(() -> {
        final long songId = this.currentSongId.get();
        final Optional<Song> optional = this.songs.stream()
            .filter(p -> p.getId() == songId).findAny();
        return optional.orElse(null);
    }, this.songs, this.currentSongId);

    private final ListProperty<Long> likedSongIds = new SimpleListProperty<>(
        this, "likedSongIds", FXCollections.observableArrayList());

    private final ListBinding<Song> likedSongs = BindingsUtils.createListBinding(() -> {
        final List<Song> list = SongService.this.likedSongIds.stream()
            .map(likedSongId -> {
                return SongService.this.songs.stream()
                    .filter(s -> s.getId() == likedSongId)
                    .findAny()
                    .orElse(null);
            })
            .filter(Objects::nonNull)
            .toList();

        return FXCollections.observableList(list);
    }, this.likedSongIds);

    private SongService(SongApiService songApiService, AuthService authService) {
        this.songApiService = songApiService;
        this.authService = authService;

        this.authService.loggedUserProperty().addListener(observable -> this.isUserDataValid.set(false));
    }

    public static SongService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void fetchData() {
        if (this.isDataValid.compareAndSet(false, true)) {
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
        if (this.isUserDataValid.compareAndSet(false, true)) {
            if (!this.authService.isLoggedIn()) {
                this.likedSongIds.setValue(FXCollections.observableArrayList());
            } else {
                fetchLikedSongIds().whenComplete((likedSongIds, error) -> {
                    if (error != null) {
                        Platform.runLater(() -> {
                            final Alert alert = new Alert(
                                Alert.AlertType.INFORMATION,
                                "Failed to fetch liked song ids\n" + error.getMessage());
                            alert.show();
                        });
                    }
                });
            }
        }
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
        this.currentSongId.set(song == null ? -1 : song.getId());
    }

    public ListProperty<Long> likedSongIdsProperty() {
        return this.likedSongIds;
    }

    public ListBinding<Song> likedSongsProperty() {
        return this.likedSongs;
    }

    public ObservableList<Song> getLikedSongs() {
        return this.likedSongs.get();
    }

    public CompletableFuture<Song[]> fetchSongs() {
        LOGGER.info("Trying to fetch songs");

        return this.songApiService.fetchSongs()
            .whenComplete((songs, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to fetch songs - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to fetch songs");

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

                    this.songs.removeIf(s -> s.getId() == song.getId());
                }
            });
    }

    public CompletableFuture<? extends Song> uploadSong(CreateSongDTO createSong, File audioFile) {
        LOGGER.info("Trying to upload song");

        return this.songApiService.uploadSong(createSong, audioFile)
            .whenComplete((song, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to upload song - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to upload song");

                    song.setUploader(UserUtils.copyDisplay(this.authService.getLoggedUser()));
                    this.songs.add(song);
                }
            });
    }

    public CompletableFuture<Long[]> fetchLikedSongIds() {
        LOGGER.info("Trying to fetch liked song ids");

        return this.songApiService.fetchLikedSongIds()
            .whenComplete((likedSongIds, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to fetch liked song ids - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to fetch liked song ids");

                    final ObservableList<Long> list =
                        FXCollections.observableArrayList(likedSongIds);
                    this.likedSongIds.setValue(list);
                }
            });
    }

    public CompletableFuture<Long> getSongLikeCount(Song song) {
        if (this.songsLikeCount.containsKey(song.getId())) {
            final AtomicLong atomicLong = this.songsLikeCount.get(song.getId());
            return CompletableFuture.completedFuture(atomicLong.get());
        } else {
            return fetchSongLikeCount(song);
        }
    }

    private CompletableFuture<Long> fetchSongLikeCount(Song song) {
        LOGGER.info("Trying to fetch song like count");

        return this.songApiService.fetchSongLikeCount(song.getId())
            .whenComplete((likeCount, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to fetch song like count - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to fetch song like count");

                    this.songsLikeCount.computeIfAbsent(song.getId(), id -> new AtomicLong(0)).set(likeCount);
                }
            });
    }

    public CompletableFuture<Void> likeSong(Song song) {
        LOGGER.info("Trying to like song");

        return this.songApiService.likeSong(song.getId())
            .whenComplete((likedSongIds, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to like song - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to like song");

                    this.likedSongIds.add(song.getId());

                    final AtomicLong songLikeCount = this.songsLikeCount.getOrDefault(song.getId(), null);
                    if (songLikeCount != null) songLikeCount.getAndIncrement();
                }
            });
    }

    public CompletableFuture<Void> unlikeSong(Song song) {
        LOGGER.info("Trying to unlike song");

        return this.songApiService.unlikeSong(song.getId())
            .whenComplete((unlikedSongIds, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to unlike song - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to unlike song");

                    this.likedSongIds.remove(song.getId());

                    final AtomicLong songLikeCount = this.songsLikeCount.getOrDefault(song.getId(), null);
                    if (songLikeCount != null) songLikeCount.getAndDecrement();
                }
            });
    }

    public CompletableFuture<Void> toggleLikeSong(Song song) {
        return isSongLiked(song) ? unlikeSong(song) : likeSong(song);
    }

    public <T extends Song> List<T> filterSongs(List<T> songs, String filter) {
        final String lowercaseFilter = filter.toLowerCase();

        return filter.isEmpty() ? songs : songs.stream().filter(song ->
            song.getName().toLowerCase().contains(lowercaseFilter) ||
            (song.getUploader() != null && song.getUploader().getDisplayName().toLowerCase().contains(lowercaseFilter)) ||
            (song.getGenre() != null && song.getGenre().getName().toLowerCase().contains(lowercaseFilter)) ||
            (song.getLanguage() != null && song.getLanguage().getName().toLowerCase().contains(lowercaseFilter))
        ).toList();
    }

    public <T extends Song> List<T> filterSongsWithAudio(List<T> songs) {
        return songs.stream().filter(song -> song.getFileName() != null).toList();
    }

    public boolean isSongLiked(Song song) {
        return this.likedSongIds.stream().anyMatch(id -> id == song.getId());
    }

    private static final class InstanceHolder {
        private static final SongService INSTANCE = new SongService(
            SongApiService.getInstance(),
            AuthService.getInstance()
        );
    }
}
