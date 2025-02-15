package org.saartako.playlist;

import org.saartako.song.SongDTO;
import org.saartako.user.User;
import org.saartako.user.UserDTO;

import java.util.Set;

public class PlaylistDTO implements Playlist {

    private long id;
    private UserDTO owner;
    private String name;
    private boolean isPrivate;
    private boolean isModifiable;
    private Set<SongDTO> songs;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public User getOwner() {
        return this.owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    @Override
    public boolean isModifiable() {
        return this.isModifiable;
    }

    public void setModifiable(boolean modifiable) {
        isModifiable = modifiable;
    }

    @Override
    public Set<SongDTO> getSongs() {
        return this.songs;
    }

    public void setSongs(Set<SongDTO> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return PlaylistUtils.toString(this);
    }
}
