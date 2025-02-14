package org.saartako.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("")
    public List<PlaylistEntity> findBy(@RequestParam("ownerId") String ownerId) {
        return playlistService.findByOwnerId(ownerId);
    }

    @GetMapping("/{id}")
    public Optional<PlaylistEntity> findById(@PathVariable("id") long id) {
        return playlistService.findById(id);
    }
}
