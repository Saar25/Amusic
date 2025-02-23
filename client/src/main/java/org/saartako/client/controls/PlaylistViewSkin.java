package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
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

    private final Button startButton = new Button("Start Playing", new FontIcon(Material2MZ.PLAY_ARROW));

    private final VBox songList = new VBox(10);

    private final ScrollPane songScrollPane = new ScrollPane(songList);

    private final GridPane gridPane = new GridPane();

    public PlaylistViewSkin(PlaylistView control) {
        super(control);

        this.songList.setPadding(new Insets(4));
        this.songScrollPane.setFitToWidth(true);

        final Button deletePlaylistButton = createDeletePlaylistButton();
        final VBox vBox = new VBox(16, deletePlaylistButton);

        GridUtils.initializeGrid(this.gridPane, 12, 12, 16, 16);

        this.gridPane.add(this.playlistCard, 0, 2, 6, 6);
        this.gridPane.add(vBox, 6, 2, 2, 6);
        this.gridPane.add(this.songScrollPane, 8, 0, 4, 12);
        this.gridPane.add(this.startButton, 0, 10, 8, 2);

        getChildren().setAll(this.gridPane);

        registerChangeListener(this.playlistService.currentPlaylistProperty(), observable ->
            updatePlaylist(this.playlistService.getCurrentPlaylist()));

        updatePlaylist(this.playlistService.getCurrentPlaylist());
    }

    private void updatePlaylist(Playlist playlist) {
        Collection<? extends Song> songs = playlist.getSongs();

        final CardItem playlistCard = PlaylistUtils.playlistToCardItem(playlist);
        this.playlistCard.setCardItem(playlistCard);

//        startButton.setOnAction(event -> startPlaying());

        final List<MusicCard> cards = songs.stream().map(song -> {
            final CardItem songCardItem = SongUtils.songToCardItem(song);
            final MusicCard songCard = new MusicCard(songCardItem);
            songCard.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                this.songService.setCurrentSong(song);
                this.routerService.push(Route.SONG_VIEW);
            });
            return songCard;
        }).toList();
        this.songList.getChildren().setAll(cards);
    }

    private Button createDeletePlaylistButton() {
        final Button deletePlaylistButton = new Button("Delete Playlist",
            new FontIcon(Material2AL.DELETE));
        deletePlaylistButton.getStyleClass().add(Styles.DANGER);

        deletePlaylistButton.setOnAction(event -> {
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

        return deletePlaylistButton;
    }
}
