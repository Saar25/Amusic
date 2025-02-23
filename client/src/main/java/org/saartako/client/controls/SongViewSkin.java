package org.saartako.client.controls;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.GridUtils;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;

import java.util.Objects;
import java.util.Optional;

public class SongViewSkin extends SkinBase<SongView> {

    private final SongService songService = SongService.getInstance();
    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final Loader loader = new Loader();

    private final MusicCard songCard = new MusicCard();

    private final Slider slider = new Slider(0, 100, 20);

    private final GridPane gridPane = new GridPane();

    public SongViewSkin(SongView control) {
        super(control);

        final Button favoriteButton = new Button("",
            new FontIcon(Material2AL.FAVORITE_BORDER));
        favoriteButton.getStyleClass().add(Styles.BUTTON_ICON);

        final Button addToPlaylistButton = new Button("Add to Playlist",
            new FontIcon(Material2AL.FEATURED_PLAY_LIST));
        addToPlaylistButton.getStyleClass().add(Styles.ACCENT);

        addToPlaylistButton.setOnAction(event -> {
            final Optional<Playlist> result = openAddToPlaylistDialog();

            result.ifPresent(playlist -> {
                final Song song = this.songService.getCurrentSong();

                this.playlistService.addPlaylistSong(playlist, song).whenComplete((response, error) -> {
                    Platform.runLater(() -> {
                        final Alert alert = error != null
                            ? new Alert(Alert.AlertType.ERROR, "Failed too add song\n" + error.getMessage())
                            : new Alert(Alert.AlertType.INFORMATION, "Added song to playlist successfully");
                        alert.showAndWait();
                    });
                });
            });
        });

        final Button deleteSongButton = createDeleteSongButton();

        final VBox vBox = new VBox(16, favoriteButton, addToPlaylistButton, deleteSongButton);

        this.slider.setSkin(new ProgressSliderSkin(this.slider));
        this.slider.getStyleClass().add(Styles.LARGE);

        GridUtils.initializeGrid(this.gridPane, 12, 12, 16, 16);

        this.gridPane.add(this.songCard, 1, 2, 6, 6);
        this.gridPane.add(vBox, 8, 2, 4, 6);
        this.gridPane.add(this.slider, 0, 11, 12, 1);

        registerChangeListener(this.songService.currentSongProperty(), observable -> updateSong());
        updateSong();
    }

    private void updateSong() {
        final Song song = this.songService.getCurrentSong();
        if (song == null) {
            Platform.runLater(() -> {
                getChildren().setAll(this.loader);
            });
        } else {
            final CardItem cardItem = SongUtils.songToCardItem(song);

            Platform.runLater(() -> {
                this.songCard.setCardItem(cardItem);

                getChildren().setAll(this.gridPane);
            });
        }
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

    private Button createDeleteSongButton() {
        final Button deleteSongButton = new Button("Delete Song",
            new FontIcon(Material2AL.DELETE));
        deleteSongButton.getStyleClass().add(Styles.DANGER);

        deleteSongButton.setOnAction(event -> {
            final Song song = this.songService.getCurrentSong();

            this.songService.deleteSong(song).whenComplete((response, error) -> {
                Platform.runLater(() -> {
                    final Alert alert;
                    if (error != null) {
                        alert = new Alert(Alert.AlertType.ERROR, "Failed too delete song\n" + error.getMessage());
                    } else {
                        alert = new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete song");
                        alert.resultProperty().addListener(o -> this.routerService.previous());
                    }
                    alert.show();
                });
            });
        });
        return deleteSongButton;
    }
}
