package org.saartako.common.song;

import org.saartako.common.genre.Genre;
import org.saartako.common.language.Language;
import org.saartako.common.user.User;

public interface Song {
    long getId();

    String getName();

    String getFileName();

    User getUploader();

    Genre getGenre();

    Language getLanguage();

    long getLengthMillis();
}
