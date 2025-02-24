package org.saartako.server.playlist;

import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.playlist.PlaylistDTO;
import org.saartako.common.playlist.PlaylistUtils;
import org.saartako.common.user.User;
import org.saartako.common.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/mine")
    public ResponseEntity<List<? extends Playlist>> findMine() {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final List<PlaylistEntity> playlistEntities = this.playlistService.findUserPlaylists(user);

        final List<? extends Playlist> body = PlaylistUtils.copyDisplay(playlistEntities);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("")
    public ResponseEntity<Playlist> createPlaylist(@RequestBody CreatePlaylistDTO playlist) {
        final UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final PlaylistEntity create = this.playlistService.create(user.getId(), playlist);

        final Playlist body = new PlaylistDTO()
            .setId(create.getId())
            .setOwner(user)
            .setName(create.getName())
            .setPrivate(create.isPrivate())
            .setModifiable(create.isModifiable())
            .setSongs(Set.of());

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") long id) {
        this.playlistService.deletePlaylist(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/song/{songId}")
    public ResponseEntity<Void> addPlaylistSong(@PathVariable("id") long id, @PathVariable("songId") long songId) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        this.playlistService.addPlaylistSong(user, id, songId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}/song/{songId}")
    public ResponseEntity<Void> deletePlaylistSong(@PathVariable("id") long id, @PathVariable("songId") long songId) {
        this.playlistService.deletePlaylistSong(id, songId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
