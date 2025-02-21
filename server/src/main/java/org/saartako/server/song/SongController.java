package org.saartako.server.song;

import org.saartako.common.song.Song;
import org.saartako.common.song.SongUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping("")
    public ResponseEntity<List<? extends Song>> findAll() {
        final List<SongEntity> songEntities = this.songService.findAll();

        final List<? extends Song> body = SongUtils.copyDisplay(songEntities);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") long id) {
        this.songService.deleteSong(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
