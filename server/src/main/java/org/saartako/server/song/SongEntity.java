package org.saartako.server.song;

import jakarta.persistence.*;
import org.saartako.common.song.Song;
import org.saartako.common.song.SongUtils;
import org.saartako.server.genre.GenreEntity;
import org.saartako.server.language.LanguageEntity;
import org.saartako.server.user.UserEntity;

@Entity(name = "songs")
public class SongEntity implements Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, unique = true, nullable = false)
    private String fileName;

    @Column(length = 64, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "uploader_id", nullable = false)
    private UserEntity uploader;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private LanguageEntity language;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UserEntity getUploader() {
        return this.uploader;
    }

    public void setUploader(UserEntity uploader) {
        this.uploader = uploader;
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
    public String toString() {
        return SongUtils.toString(this);
    }
}
