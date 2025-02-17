package org.saartako.common.song;

public class SongUtils {

    private SongUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static String toString(Song song) {
        return "SongDTO{" +
               "id=" + song.getId() +
               ", fileName='" + song.getFileName() + '\'' +
               ", name='" + song.getName() + '\'' +
               ", uploader=" + song.getUploader() +
               ", genre=" + song.getGenre() +
               ", language=" + song.getLanguage() +
               '}';
    }
}
