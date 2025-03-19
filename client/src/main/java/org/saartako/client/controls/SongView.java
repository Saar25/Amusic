package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.saartako.client.Config;
import org.saartako.client.enums.SongPlayerStatus;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.AuthService;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.BindingsUtils;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;
import org.saartako.common.user.User;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SongView extends Control implements RouteNode {

    private final SongService songService = SongService.getInstance();
    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final ObjectProperty<Duration> currentSongTime =
        new SimpleObjectProperty<>(this, "currentSongTime", Duration.ZERO);

    private final ObjectProperty<SongPlayerStatus> songPlayerStatus =
        new SimpleObjectProperty<>(this, "songPlayerStatus", SongPlayerStatus.STOPPED);

    private final BooleanBinding isSongLiked = Bindings.createBooleanBinding(() -> {
        final Song song = this.songService.currentSongProperty().get();
        if (song == null) return false;
        final ListProperty<Long> likedSongIds = this.songService.likedSongIdsProperty();
        if (likedSongIds == null) return false;
        return likedSongIds.contains(song.getId());
    }, this.songService.currentSongProperty(), this.songService.likedSongIdsProperty());

    private final BooleanBinding isSongPersonal = Bindings.createBooleanBinding(() -> {
        final Song song = this.songService.currentSongProperty().get();
        if (song == null) return false;
        final User user = this.authService.loggedUserProperty().get();
        if (user == null) return false;
        return song.getUploader().getId() == user.getId();
    }, this.songService.currentSongProperty(), this.authService.loggedUserProperty());

    private final ListBinding<Playlist> personalPlaylists = BindingsUtils.createJavaListBinding(() -> {
        final ListBinding<Playlist> playlists = this.playlistService.playlistsProperty();
        if (playlists == null) return null;
        final User user = this.authService.loggedUserProperty().get();
        if (user == null) return null;
        return playlists.stream().filter(playlist -> playlist.getOwner().getId() == user.getId()).toList();
    }, this.songService.currentSongProperty(), this.authService.loggedUserProperty());

    private final ObjectProperty<String> songAudioStreamUrl =
        new SimpleObjectProperty<>(this, "songAudioStreamUrl", null);

    @Override
    protected SongViewSkin createDefaultSkin() {
        return new SongViewSkin(this);
    }

    @Override
    public void onExistView() {
        songPlayerStatusProperty().set(SongPlayerStatus.STOPPED);
    }

    @Override
    public void onEnterView() {
        currentSongTimeProperty().set(Duration.ZERO);
        this.songAudioStreamUrl.set(null);
    }

    public ObjectBinding<Song> currentSongProperty() {
        return this.songService.currentSongProperty();
    }

    public BooleanBinding isSongLikedProperty() {
        return this.isSongLiked;
    }

    public BooleanBinding isSongPersonalProperty() {
        return this.isSongPersonal;
    }

    public ObjectProperty<Duration> currentSongTimeProperty() {
        return this.currentSongTime;
    }

    public ObjectProperty<SongPlayerStatus> songPlayerStatusProperty() {
        return this.songPlayerStatus;
    }

    public void onLikeSongButtonClick() {
        final Song song = this.songService.getCurrentSong();

        this.songService.toggleLikeSong(song);
    }

    public CompletableFuture<String> fetchSongAudioStreamUrl(Song song) {
        return this.songService.fetchSongAudioStreamUrl(song);
    }

    public void onAddToPlaylistButtonClick() {
        final Optional<Playlist> result = openAddToPlaylistDialog();

        result.ifPresent(this::addCurrentSongToPlaylist);
    }

    public void onDeleteSongButtonClick() {
        deleteCurrentSong();
    }

    private Optional<Playlist> openAddToPlaylistDialog() {
        final Dialog<Playlist> dialog = new Dialog<>();
        dialog.setTitle("Add to playlist");

        final HBox hBox = new HBox(Config.GAP_LARGE);
        hBox.setPadding(new Insets(Config.GAP_LARGE));
        hBox.setAlignment(Pos.CENTER);

        final Label playlistLabel = new Label("Choose playlist:");

        final ComboBox<Playlist> playlistComboBox = createPlaylistComboBox();

        hBox.getChildren().addAll(playlistLabel, playlistComboBox);

        dialog.getDialogPane().setContent(hBox);
        dialog.getDialogPane().getButtonTypes().addAll(
            new ButtonType("Add", ButtonBar.ButtonData.OK_DONE),
            new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));

        dialog.setResultConverter(button -> {
            if (button.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return playlistComboBox.getValue();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private ComboBox<Playlist> createPlaylistComboBox() {
        final ComboBox<Playlist> playlistComboBox = new ComboBox<>(this.personalPlaylists);
        playlistComboBox.setPlaceholder(new Label("Loading..."));
        playlistComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Playlist object) {
                return object == null ? "Choose playlist..." : object.getName();
            }

            @Override
            public Playlist fromString(String string) {
                return playlistComboBox.getItems().stream().filter(p ->
                    Objects.equals(p.getName(), string)).findAny().orElse(null);
            }
        });

        return playlistComboBox;
    }

    private void addCurrentSongToPlaylist(Playlist playlist) {
        final Song song = this.songService.getCurrentSong();

        this.playlistService.addPlaylistSong(playlist, song).whenComplete((response, error) -> {
            Platform.runLater(() -> {
                final Alert alert = error != null
                    ? new Alert(Alert.AlertType.ERROR, "Failed to add song\n" + error.getMessage())
                    : new Alert(Alert.AlertType.INFORMATION, "Added song to playlist successfully");
                alert.show();
            });
        });
    }

    private void deleteCurrentSong() {
        final Song song = this.songService.getCurrentSong();

        this.songService.deleteSong(song).whenComplete((response, error) -> {
            Platform.runLater(() -> {
                final Alert alert;
                if (error != null) {
                    alert = new Alert(Alert.AlertType.ERROR, "Failed to delete song\n" + error.getMessage());
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete song");
                    alert.resultProperty().addListener(o -> this.routerService.previous());
                }
                alert.show();
            });
        });
    }
}
