package org.saartako.server.song;

import org.saartako.common.song.Song;
import org.saartako.common.song.SongUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

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

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/audio")
    public ResponseEntity<?> getSongAudio(@PathVariable("id") long id) {
        final Optional<SongEntity> songEntityOpt = this.songService.findById(id);
        final Optional<String> filenameOpt = songEntityOpt.map(SongEntity::getFileName);
        if (filenameOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final File file = new File("../data/audio/" + filenameOpt.get());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        final String mediaType = songEntityOpt.get().getMediaType();

        final Resource resource = new FileSystemResource(file);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mediaType));
        headers.setContentDisposition(ContentDisposition.inline().filename(file.getName()).build());

        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
