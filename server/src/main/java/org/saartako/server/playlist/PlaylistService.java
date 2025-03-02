package org.saartako.server.playlist;

import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.user.User;
import org.saartako.server.song.SongEntity;
import org.saartako.server.song.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    public List<PlaylistEntity> findUserPlaylists(User user) {
        return this.playlistRepository.findPublicOrByOwnerId(user.getId());
    }

    public PlaylistEntity create(User owner, CreatePlaylistDTO createPlaylist) {
        final PlaylistEntity playlist = new PlaylistEntity();
        playlist.setName(createPlaylist.name());
        playlist.setPrivate(createPlaylist.isPrivate());
        playlist.setModifiable(createPlaylist.isModifiable());
        playlist.setOwnerId(owner.getId());
        return this.playlistRepository.save(playlist);
    }

    public void deletePlaylist(User owner, long playlistId) {
        final Optional<PlaylistEntity> playlistReference = this.playlistRepository.findById(playlistId);

        if (playlistReference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such playlist");
        } else if (playlistReference.get().getOwnerId() != owner.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Playlist is not owned by user");
        }

        this.playlistRepository.deletePlaylistFromSongs(playlistId);
        this.playlistRepository.deleteById(playlistId);
    }

    public void addPlaylistSong(User owner, long playlistId, long songId) {
        final Optional<PlaylistEntity> playlistReference = this.playlistRepository.findById(playlistId);
        final Optional<SongEntity> songReference = this.songRepository.findById(songId);

        if (playlistReference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such playlist");
        } else if (songReference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such song");
        } else if (playlistReference.get().getOwnerId() != owner.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Playlist is not owned by user");
        }

        this.playlistRepository.addPlaylistSong(playlistId, songId);
    }

    public void deletePlaylistSong(User owner, long playlistId, long songId) {
        final Optional<PlaylistEntity> playlistReference = this.playlistRepository.findById(playlistId);
        final Optional<SongEntity> songReference = this.songRepository.findById(songId);

        if (playlistReference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such playlist");
        } else if (songReference.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such song");
        } else if (playlistReference.get().getOwnerId() != owner.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Playlist is not owned by user");
        }

        this.playlistRepository.deletePlaylistSong(playlistId, songId);
    }
}