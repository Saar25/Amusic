package org.saartako.playlist;

import org.saartako.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public Optional<PlaylistEntity> findById(@PathVariable("id") long id) {
        return playlistService.findById(id);
    }
}
