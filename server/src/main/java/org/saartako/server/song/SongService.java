package org.saartako.server.song;

import org.saartako.common.user.User;
import org.saartako.server.genre.GenreEntity;
import org.saartako.server.genre.GenreRepository;
import org.saartako.server.language.LanguageEntity;
import org.saartako.server.language.LanguageRepository;
import org.saartako.server.playlist.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private LanguageRepository languageRepository;

    public List<SongEntity> findAll() {
        return this.songRepository.findAllWithGenreAndLanguage();
    }

    public Optional<SongEntity> findById(long id) {
        return this.songRepository.findById(id);
    }

    public void deleteSong(long id) {
        this.playlistRepository.deleteSongFromPlaylists(id);
        this.songRepository.deleteById(id);
    }

    public SongEntity createSong(User uploader, String name, String fileName, Long genreId, Long languageId, String mediaType) {
        final GenreEntity genre = Optional.ofNullable(genreId)
            .flatMap(this.genreRepository::findById).orElse(null);

        final LanguageEntity language = Optional.ofNullable(languageId)
            .flatMap(this.languageRepository::findById).orElse(null);

        final SongEntity song = new SongEntity();
        song.setName(name);
        song.setFileName(fileName);
        song.setUploaderId(uploader.getId());
        song.setGenre(genre);
        song.setLanguage(language);
        song.setMediaType(mediaType);

        return this.songRepository.save(song);
    }
}