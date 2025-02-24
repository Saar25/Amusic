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

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 64, unique = true)
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    private UserEntity uploader;

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

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public UserEntity getUploader() {
        return this.uploader;
    }

    @Override
    public GenreEntity getGenre() {
        return this.genre;
    }

    @Override
    public LanguageEntity getLanguage() {
        return this.language;
    }

    @Override
    public long getLengthMillis() {
        return this.lengthMillis == null ? 0 : this.lengthMillis;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    @Override
    public String toString() {
        return SongUtils.toString(this);
    }
}
