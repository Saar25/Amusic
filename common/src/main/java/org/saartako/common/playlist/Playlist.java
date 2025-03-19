package org.saartako.common.playlist;

import org.saartako.common.song.Song;
import org.saartako.common.user.User;

import java.util.Collection;

public interface Playlist {
    long getId();

    User getOwner();

    String getName();

    boolean isPrivate();

    boolean isModifiable();

    Collection<Long> getSongIds();

    Collection<? extends Song> getSongs();
}
