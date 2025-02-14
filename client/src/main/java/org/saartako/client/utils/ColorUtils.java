package org.saartako.client.utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.saartako.playlist.Playlist;
import org.saartako.song.Song;

import java.util.Random;

public class ColorUtils {

    private ColorUtils() {
    }

    public static Paint getSongColor(Song song) {
        final Random random = new Random(song.getId());

        final int red = random.nextInt(256);
        final int green = random.nextInt(256);
        final int blue = random.nextInt(256);
        return Color.rgb(red, green, blue);
    }

    public static Paint getPlaylistColor(Playlist playlist) {
        final Random random = new Random(playlist.getId());

        final int red = random.nextInt(256);
        final int green = random.nextInt(256);
        final int blue = random.nextInt(256);
        return Color.rgb(red, green, blue);
    }
}
