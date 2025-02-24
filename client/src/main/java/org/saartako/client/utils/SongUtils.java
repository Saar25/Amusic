package org.saartako.client.utils;

import javafx.scene.paint.Paint;
import org.saartako.client.models.CardItem;
import org.saartako.common.song.Song;

import java.time.Duration;
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
        if (song.getLengthMillis() != 0) {
            final Duration duration = Duration.ofMillis(song.getLengthMillis());

            details.put("Length", String.format("%02d:%02d", duration.toMinutes(), duration.toSecondsPart()));
        }

        final Paint songColor = ColorUtils.getSongColor(song);

        return new CardItem(song.getName(), details, songColor);
    }
}
