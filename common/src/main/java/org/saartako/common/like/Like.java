package org.saartako.common.like;

import org.saartako.common.song.Song;
import org.saartako.common.user.User;

public interface Like {
    long getId();

    Song getSong();

    User getUser();
}
