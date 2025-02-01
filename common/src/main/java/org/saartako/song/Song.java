package org.saartako.song;

import org.saartako.genre.Genre;
import org.saartako.language.Language;
import org.saartako.user.User;

public interface Song {
    long getId();

    String getFileName();

    String getName();

    User getUploader();

    Genre getGenre();

    Language getLanguage();
}
