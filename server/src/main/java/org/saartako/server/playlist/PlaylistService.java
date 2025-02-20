package org.saartako.server.playlist;

import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.user.User;
import org.saartako.server.exceptions.BadCredentialsException;
import org.saartako.server.song.SongEntity;
import org.saartako.server.song.SongRepository;
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
    private SongRepository songRepository;

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
        playlist.setOwnerId(ownerId);
        return this.playlistRepository.save(playlist);
    }

    public void addPlaylistSong(User owner, long playlistId, long songId) {
        final Optional<PlaylistEntity> playlistReference = this.playlistRepository.findById(playlistId);
        final Optional<SongEntity> songReference = this.songRepository.findById(songId);

        if (playlistReference.isEmpty() || songReference.isEmpty() ||
            playlistReference.get().getOwnerId() != owner.getId()) {
            throw new BadCredentialsException();
        }

        this.playlistRepository.addPlaylistSong(playlistId, songId);
    }
}