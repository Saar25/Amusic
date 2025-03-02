package org.saartako.common.like;

import org.saartako.common.song.Song;
import org.saartako.common.user.User;

public class LikeDTO implements Like {

    private long id;
    private Song song;
    private User user;

    @Override
    public long getId() {
        return this.id;
    }

    public LikeDTO setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public Song getSong() {
        return this.song;
    }

    public LikeDTO setSong(Song song) {
        this.song = song;
        return this;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    public LikeDTO setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public String toString() {
        return LikeUtils.toString(this);
    }
}
