package org.saartako.server.song;

import org.saartako.common.song.CreateSongDTO;
import org.saartako.common.user.User;
import org.saartako.common.user.UserUtils;
import org.saartako.server.genre.GenreEntity;
import org.saartako.server.genre.GenreRepository;
import org.saartako.server.language.LanguageEntity;
import org.saartako.server.language.LanguageRepository;
import org.saartako.server.like.LikeEntity;
import org.saartako.server.like.LikeRepository;
import org.saartako.server.playlist.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    private LikeRepository likeRepository;

    public List<SongEntity> findAll() {
        return this.songRepository.findAllWithGenreAndLanguage();
    }

    public Optional<SongEntity> findById(long id) {
        return this.songRepository.findById(id);
    }

    public void deleteSong(User user, long id) {
        final Optional<SongEntity> songEntityOpt = this.songRepository.findById(id);
        if (songEntityOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such song");
        }

        final SongEntity song = songEntityOpt.get();

        if (song.getUploaderId() != user.getId() && !UserUtils.isAdmin(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Song is not uploaded by user");
        }

        this.playlistRepository.deleteSongFromPlaylists(id);
        this.likeRepository.deleteSongFromLikes(id);
        this.songRepository.deleteById(id);
    }

    public SongEntity createSong(User uploader, CreateSongDTO createSongDTO) {
        final GenreEntity genre = Optional.ofNullable(createSongDTO.genreId())
            .flatMap(this.genreRepository::findById).orElse(null);

        final LanguageEntity language = Optional.ofNullable(createSongDTO.languageId())
            .flatMap(this.languageRepository::findById).orElse(null);

        final SongEntity song = new SongEntity();
        song.setName(createSongDTO.name());
        song.setFileName(null);
        song.setUploaderId(uploader.getId());
        song.setGenre(genre);
        song.setLanguage(language);
        song.setMediaType(createSongDTO.mediaType());
        song.setLengthMillis(createSongDTO.lengthMillis());

        return this.songRepository.save(song);
    }

    public void uploadSong(User uploader, Long songId, MultipartFile file) throws IOException {
        final Optional<SongEntity> songEntityOpt = this.songRepository.findById(songId);

        if (songEntityOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such song");
        }

        final SongEntity song = songEntityOpt.get();

        if (song.getUploaderId() != uploader.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Song is not uploaded by user");
        }

        uploadSong(song, file);
    }

    public void uploadSong(SongEntity song, MultipartFile file) throws IOException {
        final String fileName = UUID.randomUUID().toString().replaceAll("-", "");

        final File destination = new File("../data/audio/", fileName);
        file.transferTo(destination.toPath());

        song.setFileName(fileName);
        this.songRepository.save(song);
    }

    public void likeSong(User user, long songId) {
        final LikeEntity likeEntity = new LikeEntity();
        likeEntity.setUserId(user.getId());
        likeEntity.setSongId(songId);
        this.likeRepository.save(likeEntity);
    }

    public void unlikeSong(User user, long songId) {
        this.likeRepository.unlikeSong(user.getId(), songId);
    }
}