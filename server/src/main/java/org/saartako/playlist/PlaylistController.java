package org.saartako.playlist;

import org.saartako.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/")
    public PlaylistEntity create(@RequestBody CreatePlaylistDTO playlist) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return playlistService.create(user.getId(), playlist);
    }

    @GetMapping("/{id}")
    public Optional<PlaylistEntity> findById(@PathVariable("id") long id) {
        return playlistService.findById(id);
    }
}
