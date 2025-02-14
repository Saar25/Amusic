package org.saartako.client.controls;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.paint.Paint;
import org.saartako.client.models.CardItem;
import org.saartako.client.utils.ColorUtils;
import org.saartako.playlist.Playlist;

import java.util.Map;
import java.util.TreeMap;

public class PlaylistCardSkin implements Skin<PlaylistCard> {

    private final PlaylistCard control;

    private final MusicCard node = new MusicCard();

    public PlaylistCardSkin(PlaylistCard control) {
        this.control = control;

        this.control.playlistProperty().addListener(
            (o, prev, playlist) -> updatePlaylist(playlist));
        updatePlaylist(this.control.getPlaylist());
    }

    private void updatePlaylist(Playlist playlist) {
        final Map<String, String> details = new TreeMap<>();
        if (playlist.getOwner() != null) {
            details.put("By", playlist.getOwner().getDisplayName());
        }
        if (playlist.getSongs() != null) {
            details.put("Songs", String.valueOf(playlist.getSongs().size()));
        }

        final Paint playlistColor = ColorUtils.getPlaylistColor(playlist);

        this.node.setCardItem(new CardItem(playlist.getName(), details, playlistColor));
    }

    @Override
    public PlaylistCard getSkinnable() {
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
