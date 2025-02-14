package org.saartako.playlist;

import jakarta.persistence.*;
import org.saartako.song.SongEntity;
import org.saartako.user.UserEntity;

import java.util.Set;

@Entity(name = "playlists")
public class PlaylistEntity implements Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(length = 64, nullable = false)
    private String name;

    @Column
    private boolean isPrivate;

    @Column
    private boolean isModifiable;

    @ManyToMany
    @JoinTable(
        name = "playlist_songs",
        joinColumns = @JoinColumn(name = "playlist_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<SongEntity> songs;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public UserEntity getOwner() {
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
    public Set<SongEntity> getSongs() {
        return this.songs;
    }

    @Override
    public String toString() {
        return PlaylistUtils.toString(this);
    }
}
