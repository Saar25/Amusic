package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.saartako.client.enums.Route;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.*;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;
import org.saartako.common.user.User;

import java.util.LinkedList;
import java.util.Queue;

public class PlaylistView extends Control implements RouteNode {

    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final SongService songService = SongService.getInstance();
    private final RouterService routerService = RouterService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private final AudioService audioService = AudioService.getInstance();

    private final BooleanBinding canModifyPlaylist = Bindings.createBooleanBinding(() -> {
        final Playlist playlist = this.playlistService.currentPlaylistProperty().get();
        if (playlist == null) return false;
        final User user = this.authService.loggedUserProperty().get();
        if (user == null) return false;
        return playlist.isModifiable() && playlist.getOwner().getId() == user.getId();
    }, this.playlistService.currentPlaylistProperty(), this.authService.loggedUserProperty());

    private final ObjectProperty<Song> playedSong =
        new SimpleObjectProperty<>(this, "currentlyPlayingSong", null);

    private boolean isInView = false;

    @Override
    protected PlaylistViewSkin createDefaultSkin() {
        return new PlaylistViewSkin(this);
    }

    @Override
    public void onEnterView() {
        this.isInView = true;
    }

    @Override
    public void onExistView() {
        this.isInView = false;
        final MediaPlayer mediaPlayer = this.audioService.mediaPlayerProperty().get();
        if (mediaPlayer != null) mediaPlayer.stop();

        this.playedSong.set(null);
    }

    public ObjectBinding<Playlist> currentPlaylistProperty() {
        return this.playlistService.currentPlaylistProperty();
    }

    public ObservableObjectValue<Song> playedSongProperty() {
        return this.playedSong;
    }

    public BooleanBinding canModifyPlaylistProperty() {
        return this.canModifyPlaylist;
    }

    public void onSongExpand(Song song) {
        this.songService.setCurrentSong(song);
        this.routerService.push(Route.SONG_VIEW);
    }

    public void onDeletePlaylistButtonClick() {
        deleteCurrentPlaylist();
    }

    public void onDeleteSongFromPlaylistButtonClick(Song song) {
        deleteSongFromCurrentPlaylist(song);
    }

    private void deleteCurrentPlaylist() {
        final Playlist playlist = this.playlistService.getCurrentPlaylist();

        this.playlistService.deletePlaylist(playlist).whenComplete((response, error) -> {
            Platform.runLater(() -> {
                final Alert alert;
                if (error != null) {
                    alert = new Alert(Alert.AlertType.ERROR, "Failed to delete playlist\n" + error.getMessage());
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete playlist");
                    alert.resultProperty().addListener(o -> this.routerService.previous());
                }
                alert.show();
            });
        });
    }

    private void deleteSongFromCurrentPlaylist(Song song) {
        final Playlist playlist = this.playlistService.getCurrentPlaylist();

        this.playlistService.deletePlaylistSong(playlist, song).whenComplete((response, error) -> {
            Platform.runLater(() -> {
                final Alert alert = error != null
                    ? new Alert(Alert.AlertType.ERROR, "Failed to delete song from playlist\n" + error.getMessage())
                    : new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete song from playlist");
                alert.show();
            });
        });
    }

    public void startPlaying() {
        final Playlist playlist = currentPlaylistProperty().get();
        final Queue<? extends Song> songsQueue = new LinkedList<>(playlist.getSongs());

        final MediaPlayer mediaPlayer = this.audioService.mediaPlayerProperty().get();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
        }

        nextSong(songsQueue);
    }

    private void nextSong(Queue<? extends Song> songsQueue) {
        if (this.isInView) {
            final Song next = songsQueue.poll();

            if (next == null) {
                this.songService.setCurrentSong(null);
                this.playedSong.set(null);
            } else {
                this.songService.setCurrentSong(next);
                final MediaPlayer mediaPlayer = this.audioService.mediaPlayerProperty().get();
                if (mediaPlayer == null) {
                    nextSong(songsQueue);
                } else {
                    mediaPlayer.setOnReady(() -> this.playedSong.set(next));
                    mediaPlayer.setOnEndOfMedia(() -> nextSong(songsQueue));
                    mediaPlayer.setOnError(() -> nextSong(songsQueue));
                    mediaPlayer.play();
                }
            }
        }
    }
}
