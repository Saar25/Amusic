package org.saartako.client.utils;

import javafx.scene.paint.Paint;
import org.saartako.client.models.CardItem;
import org.saartako.playlist.Playlist;

import java.util.Map;
import java.util.TreeMap;

public class PlaylistUtils {

    private PlaylistUtils() {
    }

    public static CardItem playlistToCardItem(Playlist playlist) {
        final Map<String, String> details = new TreeMap<>();
        if (playlist.getOwner() != null) {
            details.put("By", playlist.getOwner().getDisplayName());
        }
        if (playlist.getSongs() != null) {
            details.put("Songs", String.valueOf(playlist.getSongs().size()));
        }

        final Paint playlistColor = ColorUtils.getPlaylistColor(playlist);

        return new CardItem(playlist.getName(), details, playlistColor);
    }
}
