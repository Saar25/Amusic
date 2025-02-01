package org.saartako.playlist;

import org.saartako.song.SongDTO;
import org.saartako.user.User;

import java.util.Set;

public class PlaylistDTO implements Playlist {
    private long id;
    private User owner;
    private String name;
    private boolean isPrivate;
    private boolean isModifiable;
    private Set<SongDTO> songs;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public User getOwner() {
        return this.owner;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isPrivate() {
        return this.isPrivate;
    }

    @Override
    public boolean isModifiable() {
        return this.isModifiable;
    }

    @Override
    public Set<SongDTO> getSongs() {
        return this.songs;
    }

    @Override
    public String toString() {
        return PlaylistUtils.toString(this);
    }
}
