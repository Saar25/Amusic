package org.saartako.client.controls;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.utils.PlaylistUtils;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;

import java.util.Collection;
import java.util.List;

public class PlaylistViewSkin extends SkinBase<PlaylistView> {

    private final PlaylistService playlistService = PlaylistService.getInstance();

    private final Button previousButton = new Button("← Previous");

    private final MusicCard playlistCard = new MusicCard();

    private final Button startButton = new Button("▶ Start Playing");

    private final VBox songList = new VBox(10);

    private final ScrollPane songScrollPane = new ScrollPane(songList);

    private final HBox content = new HBox(16);

    public PlaylistViewSkin(PlaylistView control) {
        super(control);

        final Region spacing = new Region();
        HBox.setHgrow(spacing, Priority.ALWAYS);

        this.songList.setPadding(new Insets(16));
        this.songScrollPane.setFitToWidth(true);

        this.content.getChildren().addAll(
            new VBox(this.previousButton, this.playlistCard, this.startButton),
            spacing,
            this.songScrollPane);
        this.content.setPadding(new Insets(16));

        getChildren().setAll(this.content);

        registerChangeListener(this.playlistService.currentPlaylistProperty(), observable ->
            updatePlaylist(this.playlistService.getCurrentPlaylist()));

        updatePlaylist(this.playlistService.getCurrentPlaylist());
    }

    private void updatePlaylist(Playlist playlist) {
        Collection<? extends Song> songs = playlist.getSongs();

        final CardItem playlistCard = PlaylistUtils.playlistToCardItem(playlist);
        this.playlistCard.setCardItem(playlistCard);

//        previousButton.setOnAction(event -> goBack());

//        startButton.setOnAction(event -> startPlaying());

        final List<MusicCard> cards = songs.stream()
            .map(SongUtils::songToCardItem)
            .map(MusicCard::new)
            .toList();
        this.songList.getChildren().setAll(cards);

    }
}
