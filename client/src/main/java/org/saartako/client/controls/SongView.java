package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.util.Duration;
import org.saartako.client.enums.SongPlayerStatus;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;

public class SongView extends Control implements RouteNode {

    private final ObjectProperty<Duration> currentSongTime =
        new SimpleObjectProperty<>(this, "currentSongTime", Duration.ZERO);

    private final ObjectProperty<SongPlayerStatus> songPlayerStatus =
        new SimpleObjectProperty<>(this, "songPlayerStatus", SongPlayerStatus.STOPPED);

    public ObjectProperty<Duration> currentSongTimeProperty() {
        return this.currentSongTime;
    }

    public ObjectProperty<SongPlayerStatus> songPlayerStatusProperty() {
        return this.songPlayerStatus;
    }

    public void addCurrentSongToPlaylist(Playlist playlist) {
        // TODO: put services as instance field, cannot right now because it ruins lazy fetching
        final PlaylistService playlistService = PlaylistService.getInstance();
        final SongService songService = SongService.getInstance();

        final Song song = songService.getCurrentSong();

        playlistService.addPlaylistSong(playlist, song).whenComplete((response, error) -> {
            Platform.runLater(() -> {
                final Alert alert = error != null
                    ? new Alert(Alert.AlertType.ERROR, "Failed too add song\n" + error.getMessage())
                    : new Alert(Alert.AlertType.INFORMATION, "Added song to playlist successfully");
                alert.show();
            });
        });
    }

    public void deleteCurrentSong() {
        // TODO: put services as instance field, cannot right now because it ruins lazy fetching
        final SongService songService = SongService.getInstance();
        final RouterService routerService = RouterService.getInstance();

        final Song song = songService.getCurrentSong();

        songService.deleteSong(song).whenComplete((response, error) -> {
            Platform.runLater(() -> {
                final Alert alert;
                if (error != null) {
                    alert = new Alert(Alert.AlertType.ERROR, "Failed too delete song\n" + error.getMessage());
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete song");
                    alert.resultProperty().addListener(o -> routerService.previous());
                }
                alert.show();
            });
        });
    }

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
        songPlayerStatusProperty().set(SongPlayerStatus.PLAYING);
    }
}
