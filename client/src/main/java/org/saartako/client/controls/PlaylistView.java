package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import org.saartako.client.enums.Route;
import org.saartako.client.services.AuthService;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;
import org.saartako.common.user.User;

public class PlaylistView extends Control {

    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final SongService songService = SongService.getInstance();
    private final RouterService routerService = RouterService.getInstance();
    private final AuthService authService = AuthService.getInstance();

    private final BooleanBinding isPlaylistPersonal = Bindings.createBooleanBinding(() -> {
        final Playlist playlist = this.playlistService.currentPlaylistProperty().get();
        if (playlist == null) return false;
        final User user = this.authService.loggedUserProperty().get();
        if (user == null) return false;
        return playlist.getOwner().getId() == user.getId();
    }, this.playlistService.currentPlaylistProperty(), this.authService.loggedUserProperty());

    private final BooleanBinding canModifyPlaylist = Bindings.createBooleanBinding(() -> {
        final Playlist playlist = this.playlistService.currentPlaylistProperty().get();
        if (playlist == null) return false;
        final User user = this.authService.loggedUserProperty().get();
        if (user == null) return false;
        return playlist.isModifiable() && playlist.getOwner().getId() == user.getId();
    }, this.playlistService.currentPlaylistProperty(), this.authService.loggedUserProperty());

    @Override
    protected PlaylistViewSkin createDefaultSkin() {
        return new PlaylistViewSkin(this);
    }

    public ObjectBinding<Playlist> currentPlaylistProperty() {
        return this.playlistService.currentPlaylistProperty();
    }

    public BooleanBinding isPlaylistPersonalProperty() {
        return this.isPlaylistPersonal;
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
                    alert = new Alert(Alert.AlertType.ERROR, "Failed too delete playlist\n" + error.getMessage());
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
                    ? new Alert(Alert.AlertType.ERROR, "Failed too delete song from playlist\n" + error.getMessage())
                    : new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete song from playlist");
                alert.show();
            });
        });
    }
}
