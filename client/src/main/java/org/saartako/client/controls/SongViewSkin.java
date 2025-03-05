package org.saartako.client.controls;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.saartako.client.Config;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.AuthService;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.GridUtils;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;
import org.saartako.common.user.User;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SongViewSkin extends SkinBase<SongView> {

    private final SongService songService = SongService.getInstance();
    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final Loader loader = new Loader();

    private final MusicCard songCard = new MusicCard();

    private final Slider slider = new Slider(0, 100, 20);

    private final Button likeSongButton;

    private final Button deleteSongButton;

    private final GridPane gridPane = new GridPane();

    private MediaPlayer mediaPlayer;

    public SongViewSkin(SongView control) {
        super(control);

        this.likeSongButton = createLikeSongButton();
        this.deleteSongButton = createDeleteSongButton();

        final VBox vBox = new VBox(Config.GAP_LARGE,
            this.likeSongButton,
            createAddToPlaylistButton(),
            this.deleteSongButton);

        this.slider.setSkin(new ProgressSliderSkin(this.slider));
        this.slider.getStyleClass().add(Styles.LARGE);
        this.slider.setMin(0);
        this.slider.setOnMousePressed(event -> {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.pause();
            }
        });
        this.slider.setOnMouseReleased(event -> {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.seek(Duration.millis(this.slider.getValue()));
                this.mediaPlayer.play();
            }
        });

        GridUtils.initializeGrid(this.gridPane, 12, 12, Config.GAP_LARGE, Config.GAP_LARGE);

        this.gridPane.add(this.songCard, 1, 2, 6, 6);
        this.gridPane.add(vBox, 8, 2, 4, 6);
        this.gridPane.add(this.slider, 0, 11, 12, 1);

        registerChangeListener(this.songService.currentSongProperty(), observable -> updateSong());
        registerChangeListener(this.authService.loggedUserProperty(), observable -> updateSong());
        registerListChangeListener(this.songService.likedSongIdsProperty(), observable -> updateSong());
        updateSong();
    }

    private void updateSong() {
        final Song song = this.songService.getCurrentSong();
        final User user = this.authService.getLoggedUser();

        if (song == null || user == null) {
            Platform.runLater(() -> {
                getChildren().setAll(this.loader);
            });
        } else {
            createMediaPlayer(song).thenAccept(mediaPlayer -> {
                if (this.mediaPlayer != null) {
                    this.mediaPlayer.dispose();
                }
                this.mediaPlayer = mediaPlayer;
                this.mediaPlayer.setAutoPlay(true);
            });

            final boolean isSongPersonal = song.getUploader().getId() == user.getId();

            final CardItem cardItem = SongUtils.songToCardItem(song);

            Platform.runLater(() -> {
                this.deleteSongButton.setVisible(isSongPersonal);
                this.deleteSongButton.setManaged(isSongPersonal);

                if (this.songService.isSongLiked(song)) {
                    this.likeSongButton.getStyleClass().addAll(Styles.DANGER);
                    this.likeSongButton.setGraphic(new FontIcon(Material2AL.FAVORITE));
                } else {
                    this.likeSongButton.getStyleClass().removeAll(Styles.DANGER);
                    this.likeSongButton.setGraphic(new FontIcon(Material2AL.FAVORITE_BORDER));
                }

                this.songCard.setCardItem(cardItem);

                this.slider.setMax(song.getLengthMillis() == 0 ? 1 : song.getLengthMillis());

                getChildren().setAll(this.gridPane);
            });
        }
    }

    private CompletableFuture<MediaPlayer> createMediaPlayer(Song song) {
        return this.songService.fetchSongAudioStreamUrl(song).thenApply(audioStreamUrl -> {
            final Media media = new Media(audioStreamUrl);

            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> {
                mediaPlayer.getError().printStackTrace();
                System.err.println("error");
            });

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                this.slider.setValue(newValue.toMillis());
            });

            return mediaPlayer;
        });
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
        final ObservableList<Playlist> playlists = this.playlistService.playlistsProperty();
        final User user = this.authService.getLoggedUser();
        final FilteredList<Playlist> personal = playlists.filtered(
            p -> p.getOwner().getId() == user.getId());

        final ComboBox<Playlist> playlistComboBox = new ComboBox<>(personal);
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

    private Button createLikeSongButton() {
        final Button button = new Button();

        button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.ROUNDED);

        button.setOnAction(event -> {
            final Song song = this.songService.getCurrentSong();

            this.songService.toggleLikeSong(song);
        });
        return button;
    }

    private Button createAddToPlaylistButton() {
        final Button button = new Button("Add to Playlist", new FontIcon(Material2AL.FEATURED_PLAY_LIST));
        button.getStyleClass().add(Styles.ACCENT);

        button.setOnAction(event -> {
            final Optional<Playlist> result = openAddToPlaylistDialog();

            result.ifPresent(playlist -> {
                final Song song = this.songService.getCurrentSong();

                this.playlistService.addPlaylistSong(playlist, song).whenComplete((response, error) -> {
                    Platform.runLater(() -> {
                        final Alert alert = error != null
                            ? new Alert(Alert.AlertType.ERROR, "Failed too add song\n" + error.getMessage())
                            : new Alert(Alert.AlertType.INFORMATION, "Added song to playlist successfully");
                        alert.show();
                    });
                });
            });
        });
        return button;
    }

    private Button createDeleteSongButton() {
        final Button button = new Button("Delete Song", new FontIcon(Material2AL.DELETE));
        button.getStyleClass().add(Styles.DANGER);

        button.setOnAction(event -> {
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
        return button;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.dispose();
        }
    }
}
