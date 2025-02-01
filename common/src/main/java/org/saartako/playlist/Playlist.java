package org.saartako.playlist;

import org.saartako.song.Song;
import org.saartako.user.User;

import java.util.Collection;

public interface Playlist {
    long getId();

    User getOwner();

    String getName();

    boolean isPrivate();

    boolean isModifiable();

    Collection<? extends Song> getSongs();
}
