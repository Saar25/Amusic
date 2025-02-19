package org.saartako.client.controls;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.PlaylistService;
import org.saartako.client.utils.PlaylistUtils;
import org.saartako.common.playlist.Playlist;

public class PlaylistViewSkin extends SkinBase<PlaylistView> {

    private final PlaylistService playlistService = PlaylistService.getInstance();

    private final MusicCard musicCard = new MusicCard();

    private final VBox node = new VBox(16);

    public PlaylistViewSkin(PlaylistView control) {
        super(control);

        this.node.getChildren().setAll(musicCard);

        registerChangeListener(this.playlistService.currentPlaylistProperty(), observable ->
            updatePlaylist(this.playlistService.getCurrentPlaylist()));

        updatePlaylist(this.playlistService.getCurrentPlaylist());

        getChildren().setAll(this.node);
    }

    private void updatePlaylist(Playlist playlist) {
        final CardItem cardItem = PlaylistUtils.playlistToCardItem(playlist);

        this.musicCard.setCardItem(cardItem);
    }
}
