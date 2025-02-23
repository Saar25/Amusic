package org.saartako.client.utils;

import javafx.scene.paint.Paint;
import org.saartako.client.models.CardItem;
import org.saartako.common.song.Song;

import java.util.Map;
import java.util.TreeMap;

public class SongUtils {

    private SongUtils() {
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

        final Paint songColor = ColorUtils.getSongColor(song);

        return new CardItem(song.getName(), details, songColor);
    }
}
