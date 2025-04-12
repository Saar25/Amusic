package org.saartako.client.utils;

import javafx.scene.paint.Paint;
import org.saartako.client.models.CardItem;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.song.Song;

import java.time.Duration;
import java.util.Map;
import java.util.TreeMap;

/**
 * Utility class for card items
 */
public class CardItemUtils {

    private CardItemUtils() {
    }

    public static CardItem songToCardItem(Song song) {
        final Map<String, String> details = new TreeMap<>();
        if (song.getUploader() != null) {
            details.put("By", song.getUploader().getDisplayName());
        }
        if (song.getGenre() != null) {
            details.put("Genre", song.getGenre().getName());
        }
        if (song.getLanguage() != null) {
            details.put("Language", song.getLanguage().getName());
        }
        if (song.getLengthMillis() != 0) {
            final Duration duration = Duration.ofMillis(song.getLengthMillis());

            details.put("Length", String.format("%02d:%02d", duration.toMinutes(), duration.toSecondsPart()));
        }

        final Paint songColor = ColorUtils.getSongColor(song);

        return new CardItem(song.getName(), details, songColor);
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
