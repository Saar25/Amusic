package org.saartako.client.services;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.saartako.client.Config;
import org.saartako.common.song.Song;

public class AudioService {

    private final SongService songService;

    private final ObjectBinding<String> mediaUrl;
    private final ObjectBinding<Media> media;
    private final ObjectBinding<MediaPlayer> mediaPlayer;

    private final ObjectProperty<MediaPlayer.Status> mediaPlayerStatus =
        new SimpleObjectProperty<>(this, "mediaPlayerStatus");

    private final ObjectProperty<Duration> mediaPlayerCurrentTime =
        new SimpleObjectProperty<>(this, "mediaPlayerCurrentTime", Duration.ZERO);

    private AudioService(SongService songService) {
        this.songService = songService;

        this.mediaUrl = Bindings.createObjectBinding(() -> {
            final Song currentSong = this.songService.currentSongProperty().get();

            // TODO: ask server for token to make streaming more secure
            return currentSong == null || currentSong.getFileName() == null ? null
                : String.format("%s/song/%d/audio", Config.serverUrl, currentSong.getId());
        }, this.songService.currentSongProperty());

        this.media = Bindings.createObjectBinding(() -> {
            final String mediaUrl = this.mediaUrl.get();

            return mediaUrl == null ? null : new Media(mediaUrl);
        }, this.songService.currentSongProperty());

        this.mediaPlayer = Bindings.createObjectBinding(() -> {
            final Media media = this.media.get();

            return media == null ? null : new MediaPlayer(media);
        }, this.media);

        this.mediaPlayer.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.mediaPlayerStatus.bind(newValue.statusProperty());
                this.mediaPlayerCurrentTime.bind(newValue.currentTimeProperty());
            } else {
                this.mediaPlayerStatus.unbind();
                this.mediaPlayerCurrentTime.unbind();
            }
        });
    }

    public static AudioService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ObjectBinding<MediaPlayer> mediaPlayerProperty() {
        return this.mediaPlayer;
    }

    public ReadOnlyObjectProperty<MediaPlayer.Status> mediaPlayerStatusProperty() {
        return this.mediaPlayerStatus;
    }

    public ReadOnlyObjectProperty<Duration> mediaPlayerCurrentTimeProperty() {
        return this.mediaPlayerCurrentTime;
    }

    private static final class InstanceHolder {
        private static final AudioService INSTANCE = new AudioService(SongService.getInstance());
    }
}
