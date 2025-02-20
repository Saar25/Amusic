package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;

import java.util.Objects;

public class SongViewSkin extends SkinBase<SongView> {

    private final SongService songService = SongService.getInstance();

    private final PlaylistService playlistService = PlaylistService.getInstance();

    private final MusicCard musicCard = new MusicCard();

    public SongViewSkin(SongView control) {
        super(control);

        registerChangeListener(this.songService.currentSongProperty(), observable -> updateSong());
        registerListChangeListener(this.playlistService.playlistsProperty(), observable -> updateSong());
        updateSong();

        getChildren().setAll(new Loader());
    }

    private void updateSong() {
        final ObservableList<Playlist> playlists = this.playlistService.getPlaylists();
        final Song song = this.songService.getCurrentSong();
        if (playlists == null || song == null) {
            return;
        }

        final CardItem cardItem = SongUtils.songToCardItem(song);

        Platform.runLater(() -> this.musicCard.setCardItem(cardItem));

        final ComboBox<Playlist> playlistComboBox = new ComboBox<>(
            FXCollections.observableArrayList(playlists));
        playlistComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Playlist object) {
                return object == null ? "..." : object.getName();
            }

            @Override
            public Playlist fromString(String string) {
                return playlistComboBox.getItems().stream().filter(p ->
                    Objects.equals(p.getName(), string)).findAny().orElse(null);
            }
        });

        final HBox content = new HBox(
            this.musicCard,
            new VBox(
                new Button("", new FontIcon(Material2AL.FAVORITE_BORDER)),
                playlistComboBox
            )
        );
        Platform.runLater(() -> getChildren().setAll(content));
    }
}
