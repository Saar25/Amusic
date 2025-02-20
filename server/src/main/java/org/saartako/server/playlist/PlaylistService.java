package org.saartako.server.playlist;

import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.user.User;
import org.saartako.server.exceptions.BadCredentialsException;
import org.saartako.server.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    public List<PlaylistEntity> findAll() {
        return this.playlistRepository.findAll();
    }

    public Optional<PlaylistEntity> findById(long id) {
        return this.playlistRepository.findById(id);
    }

    public List<PlaylistEntity> findByOwnerId(long ownerId) {
        return this.playlistRepository.findByOwnerId(ownerId);
    }

    public PlaylistEntity create(long ownerId, CreatePlaylistDTO createPlaylist) {
        final PlaylistEntity playlist = new PlaylistEntity();
        playlist.setName(createPlaylist.name());
        playlist.setPrivate(createPlaylist.isPrivate());
        playlist.setOwner(this.userRepository.getReferenceById(ownerId));
        return this.playlistRepository.save(playlist);
    }

    public void addPlaylistSong(User owner, long playlistId, long songId) {
        final PlaylistEntity playlistReference = this.playlistRepository.getReferenceById(playlistId);
        if (playlistReference.getOwner().getId() != owner.getId()) {
            throw new BadCredentialsException();
        }

        this.playlistRepository.addPlaylistSong(playlistId, songId);
    }
}