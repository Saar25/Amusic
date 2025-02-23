package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.Config;
import org.saartako.client.constants.Route;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.GridUtils;
import org.saartako.client.utils.PlaylistUtils;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;

import java.util.Collection;
import java.util.List;

public class PlaylistViewSkin extends SkinBase<PlaylistView> {

    private final PlaylistService playlistService = PlaylistService.getInstance();
    private final SongService songService = SongService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final MusicCard playlistCard = new MusicCard();

    private final VBox songList = new VBox(10);

    public PlaylistViewSkin(PlaylistView control) {
        super(control);

        this.songList.setPadding(new Insets(4));

        final ScrollPane songScrollPane = new ScrollPane(songList);
        songScrollPane.setFitToWidth(true);

        final Button deletePlaylistButton = createDeletePlaylistButton();
        final VBox vBox = new VBox(Config.GAP_LARGE, deletePlaylistButton);

        final Button startButton = new Button("Start Playing", new FontIcon(Material2MZ.PLAY_ARROW));
        startButton.setOnAction(event -> startPlaying());

        final GridPane gridPane = new GridPane();
        GridUtils.initializeGrid(gridPane, 12, 12, Config.GAP_LARGE, Config.GAP_LARGE);

        gridPane.add(this.playlistCard, 0, 2, 6, 6);
        gridPane.add(vBox, 6, 2, 2, 6);
        gridPane.add(songScrollPane, 8, 0, 4, 12);
        gridPane.add(startButton, 0, 10, 8, 2);

        getChildren().setAll(gridPane);

        registerChangeListener(this.playlistService.currentPlaylistProperty(), observable ->
            updatePlaylist(this.playlistService.getCurrentPlaylist()));

        updatePlaylist(this.playlistService.getCurrentPlaylist());
    }

    private void updatePlaylist(Playlist playlist) {
        final Collection<? extends Song> songs = playlist.getSongs();

        final CardItem playlistCard = PlaylistUtils.playlistToCardItem(playlist);

        final List<? extends Node> cards = songs.stream().map(song -> {
            final CardItem songCardItem = SongUtils.songToCardItem(song);
            final MusicCard songCard = new MusicCard(songCardItem);
            songCard.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                this.songService.setCurrentSong(song);
                this.routerService.push(Route.SONG_VIEW);
            });

            HBox.setHgrow(songCard, Priority.ALWAYS);
            final Button deletePlaylistSongButton = createDeletePlaylistSongButton(song);
            final HBox hBox = new HBox(Config.GAP_LARGE, songCard, deletePlaylistSongButton);
            hBox.setAlignment(Pos.CENTER);
            return hBox;
        }).toList();

        Platform.runLater(() -> {
            this.playlistCard.setCardItem(playlistCard);
            this.songList.getChildren().setAll(cards);
        });
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

        button.setOnAction(event -> {
            final Playlist playlist = this.playlistService.getCurrentPlaylist();

            this.playlistService.deletePlaylist(playlist).whenComplete((response, error) -> {
                Platform.runLater(() -> {
                    final Alert alert;
                    if (error != null) {
                        alert = new Alert(Alert.AlertType.ERROR, "Failed too delete playlist\n" + error.getMessage());
                    } else {
                        alert = new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete playlist");
                        alert.resultProperty().addListener(o -> this.routerService.previous());
                    }
                    alert.show();
                });
            });
        });

        return button;
    }

    private Button createDeletePlaylistSongButton(Song song) {
        final Button button = new Button("", new FontIcon(Material2AL.DELETE));
        button.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_ICON);

        button.setOnAction(event -> {
            final Playlist playlist = this.playlistService.getCurrentPlaylist();

            this.playlistService.deletePlaylistSong(playlist, song).whenComplete((response, error) -> {
                Platform.runLater(() -> {
                    final Alert alert = error != null
                        ? new Alert(Alert.AlertType.ERROR, "Failed too delete song from playlist\n" + error.getMessage())
                        : new Alert(Alert.AlertType.INFORMATION, "Succeeded to delete song from playlist");
                    alert.show();
                });
            });
        });

        return button;
    }
}
