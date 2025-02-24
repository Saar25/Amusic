package org.saartako.common.song;

import org.saartako.common.genre.Genre;
import org.saartako.common.genre.GenreDTO;
import org.saartako.common.language.Language;
import org.saartako.common.language.LanguageDTO;
import org.saartako.common.user.User;
import org.saartako.common.user.UserDTO;

public class SongDTO implements Song {

    private long id;
    private String name;
    private String fileName;
    private UserDTO uploader;
    private GenreDTO genre;
    private LanguageDTO language;
    private long lengthMillis;

    @Override
    public long getId() {
        return this.id;
    }

    public SongDTO setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public SongDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    public SongDTO setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    @Override
    public User getUploader() {
        return this.uploader;
    }

    public SongDTO setUploader(UserDTO uploader) {
        this.uploader = uploader;
        return this;
    }

    @Override
    public Genre getGenre() {
        return this.genre;
    }

    public SongDTO setGenre(GenreDTO genre) {
        this.genre = genre;
        return this;
    }

    @Override
    public Language getLanguage() {
        return this.language;
    }

    public SongDTO setLanguage(LanguageDTO language) {
        this.language = language;
        return this;
    }

    @Override
    public long getLengthMillis() {
        return this.lengthMillis;
    }

    public SongDTO setLengthMillis(long lengthMillis) {
        this.lengthMillis = lengthMillis;
        return this;
    }

    @Override
    public String toString() {
        return SongUtils.toString(this);
    }
}
