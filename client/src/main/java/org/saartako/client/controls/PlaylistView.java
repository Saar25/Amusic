package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import org.saartako.client.enums.Route;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;

public class PlaylistView extends Control {

    public void onSongExpand(Song song) {
        // TODO: put services as instance field, cannot right now because it ruins lazy fetching
        final SongService songService = SongService.getInstance();
        final RouterService routerService = RouterService.getInstance();

        songService.setCurrentSong(song);
        routerService.push(Route.SONG_VIEW);
    }

    public void deleteCurrentPlaylist() {
        // TODO: put services as instance field, cannot right now because it ruins lazy fetching
        final PlaylistService playlistService = PlaylistService.getInstance();
        final RouterService routerService = RouterService.getInstance();

        final Playlist playlist = playlistService.getCurrentPlaylist();

        playlistService.deletePlaylist(playlist).whenComplete((response, error) -> {
            Platform.runLater(() -> {
                final Alert alert;
                if (error != null) {
                    alert = new Alert(Alert.AlertType.ERROR, "Failed too delete playlist\n" + error.getMessage());
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete playlist");
                    alert.resultProperty().addListener(o -> routerService.previous());
                }
                alert.show();
            });
        });
    }

    public void deleteSongFromCurrentPlaylist(Song song) {
        // TODO: put services as instance field, cannot right now because it ruins lazy fetching
        final PlaylistService playlistService = PlaylistService.getInstance();

        final Playlist playlist = playlistService.getCurrentPlaylist();

        playlistService.deletePlaylistSong(playlist, song).whenComplete((response, error) -> {
            Platform.runLater(() -> {
                final Alert alert = error != null
                    ? new Alert(Alert.AlertType.ERROR, "Failed too delete song from playlist\n" + error.getMessage())
                    : new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete song from playlist");
                alert.show();
            });
        });
    }

    @Override
    protected PlaylistViewSkin createDefaultSkin() {
        return new PlaylistViewSkin(this);
    }
}
