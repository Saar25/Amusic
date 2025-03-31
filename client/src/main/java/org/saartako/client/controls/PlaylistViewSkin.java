package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.Config;
import org.saartako.client.components.Loader;
import org.saartako.client.events.CardItemEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.models.MenuAction;
import org.saartako.client.utils.CardItemUtils;
import org.saartako.client.utils.GridUtils;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;

import java.util.Collection;
import java.util.List;

public class PlaylistViewSkin extends SkinBase<PlaylistView> {

    private final Loader loader = new Loader();

    private final MusicCard playlistCard = new MusicCard();

    private final VBox songList = new VBox(Config.GAP_MEDIUM);

    private final Button deletePlaylistButton;

    private final GridPane gridPane = new GridPane();

    public PlaylistViewSkin(PlaylistView control) {
        super(control);

        this.songList.setPadding(new Insets(Config.GAP_SMALL));

        final ScrollPane songScrollPane = new ScrollPane(songList);
        songScrollPane.setFitToWidth(true);

        this.deletePlaylistButton = createDeletePlaylistButton();
        final VBox actionsVBox = new VBox(Config.GAP_LARGE, deletePlaylistButton);

        final Button playButton = createPlayButton();

        final Label listeningToLabel = createListeningToLabel();

        final HBox listenHBox = new HBox(Config.GAP_LARGE, playButton, listeningToLabel);
        listenHBox.setAlignment(Pos.CENTER_LEFT);

        GridUtils.initializeGrid(this.gridPane, 12, 12, Config.GAP_LARGE, Config.GAP_LARGE);

        this.gridPane.add(this.playlistCard, 0, 2, 6, 6);
        this.gridPane.add(actionsVBox, 6, 2, 2, 6);
        this.gridPane.add(songScrollPane, 8, 0, 4, 12);
        this.gridPane.add(listenHBox, 0, 10, 8, 2);

        getChildren().setAll(this.gridPane);

        registerChangeListener(getSkinnable().canModifyPlaylistProperty(), observable -> updatePlaylist());
        registerChangeListener(getSkinnable().currentPlaylistProperty(), observable -> updatePlaylist());
        updatePlaylist();

        registerChangeListener(getSkinnable().canDeletePlaylistProperty(), observable -> updateCanDeletePlaylist());
        updateCanDeletePlaylist();
    }

    private void updatePlaylist() {
        final Playlist playlist = getSkinnable().currentPlaylistProperty().get();

        if (playlist == null) {
            Platform.runLater(() -> {
                getChildren().setAll(this.loader);
            });
        } else {
            final boolean isPlaylistModifiable = getSkinnable().canModifyPlaylistProperty().get();
            this.deletePlaylistButton.setVisible(isPlaylistModifiable);
            this.deletePlaylistButton.setManaged(isPlaylistModifiable);

            final CardItem playlistCard = CardItemUtils.playlistToCardItem(playlist);

            final Collection<? extends Song> songs = playlist.getSongs();
            final List<? extends Node> cards = songs.stream().map(this::songToCardItem).toList();

            Platform.runLater(() -> {
                this.playlistCard.setCardItem(playlistCard);
                this.songList.getChildren().setAll(cards);
                getChildren().setAll(this.gridPane);
            });
        }
    }

    private void updateCanDeletePlaylist() {
        final boolean canDeletePlaylist = getSkinnable().canDeletePlaylistProperty().get();

        Platform.runLater(() -> {
            this.deletePlaylistButton.setVisible(canDeletePlaylist);
            this.deletePlaylistButton.setManaged(canDeletePlaylist);
        });
    }

    private MusicCard songToCardItem(Song song) {
        final boolean isPlaylistModifiable = getSkinnable().canModifyPlaylistProperty().get();
        final CardItem songCardItem = CardItemUtils.songToCardItem(song);
        final MusicCard songCard = new MusicCard(songCardItem);
        songCard.setExpandable(true);

        if (isPlaylistModifiable) {
            songCard.getMenuActions().setAll(
                new MenuAction("Delete from playlist", event -> {
                    getSkinnable().onDeleteSongFromPlaylistButtonClick(song);
                })
            );
        }
        songCard.addEventFilter(CardItemEvent.EXPAND_CARD_ITEM, e ->
            getSkinnable().onSongExpand(song)
        );
        return songCard;
    }

    private Button createDeletePlaylistButton() {
        final Button button = new Button("Delete Playlist", new FontIcon(Material2AL.DELETE));
        button.getStyleClass().add(Styles.DANGER);
        button.setOnAction(event -> getSkinnable().onDeletePlaylistButtonClick());
        return button;
    }

    private Button createPlayButton() {
        final Button playButton = new Button("Start Playing", new FontIcon(Material2MZ.PLAY_ARROW));
        HBox.setHgrow(playButton, Priority.ALWAYS);

        playButton.textProperty().bind(Bindings.createStringBinding(() -> {
            final boolean isPlaying = getSkinnable().isPlayingProperty().get();
            return isPlaying ? "Next Song" : "Start Playing";
        }, getSkinnable().isPlayingProperty()));

        playButton.setOnAction(event -> {
            final boolean isPlaying = getSkinnable().isPlayingProperty().get();
            if (isPlaying) {
                getSkinnable().nextSong();
            } else {
                getSkinnable().startPlaying();
            }
        });
        return playButton;
    }

    private Label createListeningToLabel() {
        final Label listeningToLabel = new Label();
        listeningToLabel.getStyleClass().addAll(Styles.ACCENT, Styles.TEXT_BOLD);
        listeningToLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            final Song playedSong = getSkinnable().playedSongProperty().get();

            return playedSong == null
                ? "Not listening to any song at the moment"
                : "Currently listening to: " + playedSong.getName();
        }, getSkinnable().playedSongProperty()));

        return listeningToLabel;
    }
}
