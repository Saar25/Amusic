package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
import java.util.Optional;

public class SongViewSkin extends SkinBase<SongView> {

    private final SongService songService = SongService.getInstance();

    private final PlaylistService playlistService = PlaylistService.getInstance();

    private final MusicCard musicCard = new MusicCard();

    public SongViewSkin(SongView control) {
        super(control);

        registerChangeListener(this.songService.currentSongProperty(), observable -> updateSong());
        updateSong();

        getChildren().setAll(new Loader());
    }

    private void updateSong() {
        final Song song = this.songService.getCurrentSong();
        if (song == null) {
            return;
        }

        final CardItem cardItem = SongUtils.songToCardItem(song);

        final Button favoriteButton = new Button("",
            new FontIcon(Material2AL.FAVORITE_BORDER));
        favoriteButton.getStyleClass().add(Styles.BUTTON_ICON);

        final Button addToPlaylistButton = new Button("Add to Playlist",
            new FontIcon(Material2AL.FEATURED_PLAY_LIST));
        addToPlaylistButton.getStyleClass().add(Styles.ACCENT);

        addToPlaylistButton.setOnAction(event -> {
            final Optional<Playlist> result = openAddToPlaylistDialog();

            result.ifPresent(playlist ->
                this.playlistService.addPlaylistSong(playlist, song).whenComplete((response, error) ->
                    Platform.runLater(() -> {
                        final Alert alert = error != null
                            ? new Alert(Alert.AlertType.ERROR, "Failed too add song\n" + error.getMessage())
                            : new Alert(Alert.AlertType.INFORMATION, "Added song to playlist successfully");
                        alert.showAndWait();
                    })));
        });

        Platform.runLater(() -> {
            this.musicCard.setCardItem(cardItem);

            final HBox content = new HBox(
                this.musicCard,
                new VBox(favoriteButton, addToPlaylistButton)
            );
            getChildren().setAll(content);
        });
    }

    private Optional<Playlist> openAddToPlaylistDialog() {
        final Dialog<Playlist> dialog = new Dialog<>();
        dialog.setTitle("Add to playlist");

        final HBox hBox = new HBox(16);
        hBox.setPadding(new Insets(16));
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
        final ListProperty<Playlist> playlists = this.playlistService.playlistsProperty();

        final ComboBox<Playlist> playlistComboBox = new ComboBox<>(playlists);
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
}
