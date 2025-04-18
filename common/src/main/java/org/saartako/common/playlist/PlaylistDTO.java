package org.saartako.common.playlist;

import com.google.gson.annotations.SerializedName;
import org.saartako.common.song.SongDTO;
import org.saartako.common.user.User;
import org.saartako.common.user.UserDTO;

import java.util.Set;

public class PlaylistDTO implements Playlist {

    private long id;

    private UserDTO owner;

    private String name;

    @SerializedName("private")
    private boolean isPrivate;

    @SerializedName("modifiable")
    private boolean isModifiable;

    private Set<Long> songIds;

    private Set<SongDTO> songs;

    @Override
    public long getId() {
        return this.id;
    }

    public PlaylistDTO setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public User getOwner() {
        return this.owner;
    }

    public PlaylistDTO setOwner(UserDTO owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public PlaylistDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean isPrivate() {
        return this.isPrivate;
    }

    public PlaylistDTO setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        return this;
    }

    @Override
    public boolean isModifiable() {
        return this.isModifiable;
    }

    public PlaylistDTO setModifiable(boolean modifiable) {
        this.isModifiable = modifiable;
        return this;
    }

    @Override
    public Set<Long> getSongIds() {
        return this.songIds;
    }

    public PlaylistDTO setSongIds(Set<Long> songIds) {
        this.songIds = songIds;
        return this;
    }

    @Override
    public Set<SongDTO> getSongs() {
        return this.songs;
    }

    public PlaylistDTO setSongs(Set<SongDTO> songs) {
        this.songs = songs;
        return this;
    }

    @Override
    public String toString() {
        return PlaylistUtils.toString(this);
    }
}
