package org.saartako.server.song;

import jakarta.persistence.*;
import org.saartako.common.song.Song;
import org.saartako.common.song.SongUtils;
import org.saartako.server.genre.GenreEntity;
import org.saartako.server.language.LanguageEntity;
import org.saartako.server.like.LikeEntity;
import org.saartako.server.user.UserEntity;

import java.util.List;

@Entity(name = "songs")
public class SongEntity implements Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 32, unique = true)
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false, insertable = false, updatable = false)
    private UserEntity uploader;

    @Column(name = "uploader_id", nullable = false)
    private Long uploaderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private LanguageEntity language;

    @Column(length = 16)
    private String mediaType;

    @Column
    private Long lengthMillis;

    @OneToMany(mappedBy = "song", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LikeEntity> likes;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public UserEntity getUploader() {
        return this.uploader;
    }

    public Long getUploaderId() {
        return this.uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    @Override
    public GenreEntity getGenre() {
        return this.genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }

    @Override
    public LanguageEntity getLanguage() {
        return this.language;
    }

    public void setLanguage(LanguageEntity language) {
        this.language = language;
    }

    @Override
    public long getLengthMillis() {
        return this.lengthMillis == null ? 0 : this.lengthMillis;
    }

    public void setLengthMillis(Long lengthMillis) {
        this.lengthMillis = lengthMillis;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public String toString() {
        return SongUtils.toString(this);
    }
}
