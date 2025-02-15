package org.saartako.playlist;

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

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/mine")
    public List<PlaylistEntity> findAll() {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return playlistService.findByOwnerId(user.getId());
    }

    @PostMapping("")
    public ResponseEntity<Playlist> create(@RequestBody CreatePlaylistDTO playlist) {
        final UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final PlaylistEntity create = this.playlistService.create(user.getId(), playlist);

        final PlaylistDTO body = new PlaylistDTO();
        body.setId(create.getId());
        body.setOwner(user);
        body.setName(create.getName());
        body.setPrivate(create.isPrivate());
        body.setModifiable(create.isModifiable());
        body.setSongs(Set.of());

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/{id}")
    public Optional<PlaylistEntity> findById(@PathVariable("id") long id) {
        return playlistService.findById(id);
    }
}
