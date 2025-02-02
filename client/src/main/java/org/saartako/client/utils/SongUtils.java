package org.saartako.client.utils;

import org.saartako.song.Song;

public class SongUtils {

    private SongUtils() {
    }

    public static String getSongColor(Song song) {
        final long id = song == null ? 0 : song.getId();
        return "-color-accent-" + (id % 5 + 5);
    }
}
