package org.saartako.playlist;

import org.saartako.genre.GenreDTO;
import org.saartako.language.LanguageDTO;
import org.saartako.song.SongDTO;
import org.saartako.user.User;
import org.saartako.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
                    .setId(playlistEntity.getOwner().getId())
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
                        .setGenre(new GenreDTO()
                            .setId(songEntity.getGenre().getId())
                            .setName(songEntity.getGenre().getName())
                        )
                        .setLanguage(new LanguageDTO()
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

    @GetMapping("/{id}")
    public Optional<PlaylistEntity> findById(@PathVariable("id") long id) {
        return this.playlistService.findById(id);
    }
}
