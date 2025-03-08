package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.Config;
import org.saartako.client.events.CardItemEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.models.MenuAction;
import org.saartako.client.utils.GridUtils;
import org.saartako.client.utils.PlaylistUtils;
import org.saartako.client.utils.SongUtils;
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

        final Button startButton = new Button("Start Playing", new FontIcon(Material2MZ.PLAY_ARROW));
        startButton.setOnAction(event -> startPlaying());

        GridUtils.initializeGrid(this.gridPane, 12, 12, Config.GAP_LARGE, Config.GAP_LARGE);

        this.gridPane.add(this.playlistCard, 0, 2, 6, 6);
        this.gridPane.add(actionsVBox, 6, 2, 2, 6);
        this.gridPane.add(songScrollPane, 8, 0, 4, 12);
        this.gridPane.add(startButton, 0, 10, 8, 2);

        getChildren().setAll(this.gridPane);

        registerChangeListener(getSkinnable().canModifyPlaylistProperty(), observable -> updatePlaylist());
        registerChangeListener(getSkinnable().currentPlaylistProperty(), observable -> updatePlaylist());
        updatePlaylist();
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

            final Collection<? extends Song> songs = playlist.getSongs();

            final CardItem playlistCard = PlaylistUtils.playlistToCardItem(playlist);

            final List<? extends Node> cards = songs.stream().map(song -> {
                final CardItem songCardItem = SongUtils.songToCardItem(song);
                final MusicCard songCard = new MusicCard(songCardItem);
                songCard.setExpandable(true);
                if (isPlaylistModifiable) {
                    songCard.getMenuActions().setAll(
                        new MenuAction("Delete from playlist", event ->
                            getSkinnable().onDeleteSongFromPlaylistButtonClick(song))
                    );
                }
                songCard.addEventFilter(CardItemEvent.EXPAND_CARD_ITEM, e ->
                    getSkinnable().onSongExpand(song)
                );
                return songCard;
            }).toList();

            Platform.runLater(() -> {
                this.playlistCard.setCardItem(playlistCard);
                this.songList.getChildren().setAll(cards);
                getChildren().setAll(this.gridPane);
            });
        }
    }

    public void startPlaying() {
        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.WARNING, "Behaviour not implemented!");
            alert.show();
        });
    }

    private Button createDeletePlaylistButton() {
        final Button button = new Button("Delete Playlist", new FontIcon(Material2AL.DELETE));
        button.getStyleClass().add(Styles.DANGER);
        button.setOnAction(event -> getSkinnable().onDeletePlaylistButtonClick());
        return button;
    }
}
