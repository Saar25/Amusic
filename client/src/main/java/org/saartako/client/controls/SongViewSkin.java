package org.saartako.client.controls;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.Config;
import org.saartako.client.components.Loader;
import org.saartako.client.models.CardItem;
import org.saartako.client.utils.CardItemUtils;
import org.saartako.client.utils.GridUtils;
import org.saartako.common.song.Song;

/**
 * Skin for {@link SongView}
 */
public class SongViewSkin extends SkinBase<SongView> {

    private static final FontIcon NOT_FAVORITE_GRAPHIC = new FontIcon(Material2AL.FAVORITE_BORDER);
    private static final FontIcon FAVORITE_GRAPHIC = new FontIcon(Material2AL.FAVORITE);
    private static final FontIcon DELETE_GRAPHIC = new FontIcon(Material2AL.DELETE);
    private static final FontIcon PLAY_GRAPHIC = new FontIcon(Material2MZ.PLAY_ARROW);
    private static final FontIcon ERROR_GRAPHIC = new FontIcon(Material2AL.ERROR);

    private final Loader loader = new Loader();

    private final MusicCard songCard = new MusicCard();

    private final Slider slider = new Slider();

    private final Button playButton;

    private final Button likeSongButton;

    private final Button deleteSongButton;

    private final GridPane gridPane = new GridPane();

    public SongViewSkin(SongView control) {
        super(control);

        this.likeSongButton = createLikeSongButton();
        this.deleteSongButton = createDeleteSongButton();

        final Label likeCountLabel = new Label();
        likeCountLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            final long songLikeCount = getSkinnable().songLikeCountProperty().get();
            return songLikeCount == -1 ? "" : songLikeCount + " Likes";
        }, getSkinnable().songLikeCountProperty()));

        final HBox likesHBox = new HBox(Config.GAP_MEDIUM, this.likeSongButton, likeCountLabel);
        likesHBox.setAlignment(Pos.CENTER_LEFT);

        final VBox vBox = new VBox(Config.GAP_LARGE,
            likesHBox,
            createAddToPlaylistButton(),
            this.deleteSongButton);

        this.slider.setSkin(new ProgressSliderSkin(this.slider));
        this.slider.getStyleClass().add(Styles.LARGE);
        this.slider.setMin(0);
        this.slider.setOnMouseReleased(event -> {
            final double value = this.slider.getValue();
            final Duration duration = Duration.millis(value);
            final MediaPlayer mediaPlayer = getSkinnable().mediaPlayerProperty().get();
            mediaPlayer.seek(duration);
        });
        HBox.setHgrow(this.slider, Priority.ALWAYS);

        this.playButton = createPlayButton();
        final HBox controlBox = new HBox(Config.GAP_MEDIUM, this.playButton, this.slider);
        controlBox.setAlignment(Pos.CENTER);

        GridUtils.initializeGrid(this.gridPane, 12, 12, Config.GAP_LARGE, Config.GAP_LARGE);

        this.gridPane.add(this.songCard, 1, 2, 6, 6);
        this.gridPane.add(vBox, 8, 2, 4, 6);
        this.gridPane.add(controlBox, 0, 11, 12, 1);

        registerChangeListener(getSkinnable().mediaPlayerStatusProperty(), observable -> updatePlayerStatus());
        updatePlayerStatus();

        registerChangeListener(getSkinnable().mediaPlayerCurrentTimeProperty(), observable -> updatePlayerCurrentTime());
        updatePlayerCurrentTime();

        registerChangeListener(getSkinnable().currentSongProperty(), observable -> updateCurrentSong());
        updateCurrentSong();

        registerChangeListener(getSkinnable().isSongLikedProperty(), observable -> updateIsSongLiked());
        updateIsSongLiked();

        registerChangeListener(getSkinnable().canDeleteSongProperty(), observable -> updateCanDeleteSong());
        updateCanDeleteSong();
    }

    private void updatePlayerStatus() {
        final MediaPlayer mediaPlayer = getSkinnable().mediaPlayerProperty().get();

        if (mediaPlayer == null || mediaPlayer.getStatus() == null) {
            this.playButton.setGraphic(ERROR_GRAPHIC);
            this.playButton.getStyleClass().removeAll(Styles.DANGER);
            this.playButton.getStyleClass().addAll(Styles.DANGER);
            this.playButton.setDisable(true);
            this.slider.setDisable(true);
        } else {
            switch (mediaPlayer.getStatus()) {
                case PLAYING -> {
                    this.playButton.setGraphic(new FontIcon(Material2MZ.PAUSE));
                    this.playButton.getStyleClass().removeAll(Styles.DANGER);
                }
                case PAUSED, STOPPED -> {
                    this.playButton.setGraphic(PLAY_GRAPHIC);
                    this.playButton.getStyleClass().removeAll(Styles.DANGER);
                }
                case UNKNOWN, HALTED, DISPOSED -> {
                    this.playButton.setGraphic(ERROR_GRAPHIC);
                    this.playButton.getStyleClass().removeAll(Styles.DANGER);
                    this.playButton.getStyleClass().addAll(Styles.DANGER);
                    this.playButton.setDisable(true);
                    this.slider.setDisable(true);
                }
                case READY -> {
                    this.playButton.setGraphic(PLAY_GRAPHIC);
                    this.playButton.getStyleClass().removeAll(Styles.DANGER);
                    this.playButton.setDisable(false);
                    this.slider.setDisable(false);
                }
            }
        }
    }

    private void updatePlayerCurrentTime() {
        final Duration newTime = getSkinnable().mediaPlayerCurrentTimeProperty().get();
        final boolean isValueChanging = this.slider.isValueChanging() || this.slider.isPressed();
        if (!isValueChanging) {
            this.slider.setValue(newTime.toMillis());
        }
    }

    private void updateCurrentSong() {
        final Song song = getSkinnable().currentSongProperty().get();

        if (song == null) {
            Platform.runLater(() -> {
                getChildren().setAll(this.loader);
            });
        } else {
            final CardItem cardItem = CardItemUtils.songToCardItem(song);

            Platform.runLater(() -> {
                this.songCard.setCardItem(cardItem);

                this.slider.setMax(song.getLengthMillis() == 0 ? 1 : song.getLengthMillis());

                getChildren().setAll(this.gridPane);
                updatePlayerStatus();
            });
        }
    }

    private void updateIsSongLiked() {
        final boolean isSongLiked = getSkinnable().isSongLikedProperty().get();

        Platform.runLater(() -> {
            if (isSongLiked) {
                this.likeSongButton.getStyleClass().addAll(Styles.DANGER);
                this.likeSongButton.setGraphic(FAVORITE_GRAPHIC);
            } else {
                this.likeSongButton.getStyleClass().removeAll(Styles.DANGER);
                this.likeSongButton.setGraphic(NOT_FAVORITE_GRAPHIC);
            }
        });
    }

    private void updateCanDeleteSong() {
        final boolean canDeleteSong = getSkinnable().canDeleteSongProperty().get();

        Platform.runLater(() -> {
            this.deleteSongButton.setVisible(canDeleteSong);
            this.deleteSongButton.setManaged(canDeleteSong);
        });
    }

    private Button createPlayButton() {
        final Button playButton = new Button(null, new FontIcon(Material2MZ.PAUSE));
        playButton.getStyleClass().addAll(Styles.FLAT, Styles.LARGE);
        playButton.setOnAction(event -> {
            final MediaPlayer mediaPlayer = getSkinnable().mediaPlayerProperty().get();
            if (mediaPlayer != null) {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.play();
                }
            }
        });
        return playButton;
    }

    private Button createLikeSongButton() {
        final Button button = new Button(null, FAVORITE_GRAPHIC);
        button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.ROUNDED);
        button.setOnAction(event -> getSkinnable().onLikeSongButtonClick());
        return button;
    }

    private Button createAddToPlaylistButton() {
        final Button button = new Button("Add to Playlist", new FontIcon(Material2AL.FEATURED_PLAY_LIST));
        button.getStyleClass().add(Styles.ACCENT);
        button.setOnAction(event -> getSkinnable().onAddToPlaylistButtonClick());
        return button;
    }

    private Button createDeleteSongButton() {
        final Button button = new Button("Delete Song", DELETE_GRAPHIC);
        button.getStyleClass().add(Styles.DANGER);
        button.setOnAction(event -> getSkinnable().onDeleteSongButtonClick());
        return button;
    }
}
