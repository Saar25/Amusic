package org.saartako.song;

import org.saartako.genre.Genre;
import org.saartako.language.Language;
import org.saartako.user.User;

public class SongDTO implements Song {
    private long id;
    private String fileName;
    private String name;
    private User uploader;
    private Genre genre;
    private Language language;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public User getUploader() {
        return this.uploader;
    }

    @Override
    public Genre getGenre() {
        return this.genre;
    }

    @Override
    public Language getLanguage() {
        return this.language;
    }

    @Override
    public String toString() {
        return SongUtils.toString(this);
    }
}
