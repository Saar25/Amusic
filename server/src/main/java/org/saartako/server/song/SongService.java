package org.saartako.server.song;

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
}