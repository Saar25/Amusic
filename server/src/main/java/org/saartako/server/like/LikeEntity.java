package org.saartako.server.like;

import jakarta.persistence.*;
import org.saartako.common.like.Like;
import org.saartako.common.like.LikeUtils;
import org.saartako.server.song.SongEntity;
import org.saartako.server.user.UserEntity;

@Entity(name = "likes")
public class LikeEntity implements Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false, insertable = false, updatable = false)
    private SongEntity song;

    @Column(name = "song_id", nullable = false)
    private Long songId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public SongEntity getSong() {
        return this.song;
    }

    public Long getSongId() {
        return this.songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    @Override
    public UserEntity getUser() {
        return this.user;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return LikeUtils.toString(this);
    }
}
