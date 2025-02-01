package org.saartako.song;

import jakarta.persistence.*;
import org.saartako.genre.Genre;
import org.saartako.language.Language;
import org.saartako.user.User;

@Entity(name = "songs")
public class SongEntity implements Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 16, unique = true, nullable = false)
    private String fileName;

    @Column(length = 16, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

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
    public User getUploader() {
        return this.uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    @Override
    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return SongUtils.toString(this);
    }
}
