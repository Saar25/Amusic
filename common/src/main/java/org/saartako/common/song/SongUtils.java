package org.saartako.common.song;

import org.saartako.common.genre.GenreUtils;
import org.saartako.common.language.LanguageUtils;
import org.saartako.common.user.UserUtils;

import java.util.List;

public class SongUtils {

    private SongUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static List<SongDTO> copyDisplay(List<? extends Song> songs) {
        return songs.stream().map(SongUtils::copyDisplay).toList();
    }

    public static SongDTO copyDisplay(Song song) {
        return new SongDTO()
            .setId(song.getId())
            .setName(song.getName())
            .setFileName(song.getFileName())
            .setUploader(UserUtils.copyDisplay(song.getUploader()))
            .setGenre(GenreUtils.copy(song.getGenre()))
            .setLanguage(LanguageUtils.copy(song.getLanguage()))
            .setLengthMillis(song.getLengthMillis());
    }

    public static String toString(Song song) {
        return "Song{" +
               "id=" + song.getId() +
               ", fileName='" + song.getFileName() + '\'' +
               ", name='" + song.getName() + '\'' +
               ", uploader=" + song.getUploader() +
               ", genre=" + song.getGenre() +
               ", language=" + song.getLanguage() +
               '}';
    }
}
