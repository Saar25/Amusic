package org.saartako.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    public List<PlaylistEntity> findAll() {
        return this.playlistRepository.findAll();
    }

    public Optional<PlaylistEntity> findById(long id) {
        return this.playlistRepository.findById(id);
    }

    public List<PlaylistEntity> findByOwnerId(String ownerId) {
        return this.playlistRepository.findByOwnerId(ownerId);
    }
}