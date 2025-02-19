package org.saartako.client.controls;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.VBox;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.utils.PlaylistUtils;
import org.saartako.common.playlist.Playlist;

public class PlaylistViewSkin implements Skin<PlaylistView> {

    private final PlaylistView control;

    private final MusicCard musicCard = new MusicCard();

    private final VBox node = new VBox(16);

    public PlaylistViewSkin(PlaylistView control) {
        this.control = control;

        this.node.getChildren().setAll(musicCard);

        final PlaylistService playlistService = PlaylistService.getInstance();

        playlistService.currentPlaylistProperty().addListener((observable, oldValue, newValue) ->
            updatePlaylist(newValue));

        updatePlaylist(playlistService.getCurrentPlaylist());
    }

    private void updatePlaylist(Playlist playlist) {
        final CardItem cardItem = PlaylistUtils.playlistToCardItem(playlist);

        this.musicCard.setCardItem(cardItem);
    }

    @Override
    public PlaylistView getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
    }
}
