package org.saartako.server.playlist;

import org.saartako.common.genre.GenreDTO;
import org.saartako.common.language.LanguageDTO;
import org.saartako.common.playlist.CreatePlaylistDTO;
import org.saartako.common.playlist.Playlist;
import org.saartako.common.playlist.PlaylistDTO;
import org.saartako.common.song.SongDTO;
import org.saartako.common.user.User;
import org.saartako.common.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/mine")
    public ResponseEntity<List<? extends Playlist>> findAll() {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final List<PlaylistEntity> playlistEntities = this.playlistService.findByOwnerId(user.getId());

        final List<? extends Playlist> body = playlistEntities.stream().map(playlistEntity ->
            new PlaylistDTO()
                .setId(playlistEntity.getId())
                .setOwner(new UserDTO()
                    .setId(playlistEntity.getOwnerId())
                    .setDisplayName(playlistEntity.getOwner().getDisplayName())
                )
                .setName(playlistEntity.getName())
                .setPrivate(playlistEntity.isPrivate())
                .setModifiable(playlistEntity.isModifiable())
                .setSongs(playlistEntity.getSongs().stream().map(songEntity ->
                    new SongDTO()
                        .setId(songEntity.getId())
                        .setName(songEntity.getName())
                        .setFileName(songEntity.getFileName())
                        .setUploader(
                            new UserDTO()
                                .setId(songEntity.getUploader().getId())
                                .setDisplayName(songEntity.getUploader().getDisplayName())
                        )
                        .setGenre(songEntity.getGenre() == null ? null : new GenreDTO()
                            .setId(songEntity.getGenre().getId())
                            .setName(songEntity.getGenre().getName())
                        )
                        .setLanguage(songEntity.getLanguage() == null ? null : new LanguageDTO()
                            .setId(songEntity.getLanguage().getId())
                            .setName(songEntity.getLanguage().getName())
                        )
                ).collect(Collectors.toSet()))
        ).toList();

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("")
    public ResponseEntity<Playlist> create(@RequestBody CreatePlaylistDTO playlist) {
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

    @PostMapping("/{id}/song/{songId}")
    public ResponseEntity<Void> addPlaylistSong(@PathVariable("id") long id, @PathVariable("songId") long songId) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        this.playlistService.addPlaylistSong(user, id, songId);
        return ResponseEntity.ok(null);
    }
}
