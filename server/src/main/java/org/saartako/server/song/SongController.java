package org.saartako.server.song;

import org.saartako.common.song.CreateSongDTO;
import org.saartako.common.song.Song;
import org.saartako.common.song.SongUtils;
import org.saartako.common.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        final List<SongEntity> songEntities = this.songService.findAll();

        final Set<? extends Song> body = SongUtils.copyDisplay(songEntities);

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

    @PostMapping("")
    public ResponseEntity<?> createSong(@RequestBody CreateSongDTO createSongDTO) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            final SongEntity song = this.songService.createSong(user, createSongDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(song);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to save song");
        }
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createSong(@RequestPart("song") CreateSongDTO createSongDTO,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final SongEntity song;
        try {
            song = this.songService.createSong(user, createSongDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to save song");
        }

        try {
            if (file != null && !file.isEmpty()) {
                this.songService.uploadSong(user, song.getId(), file);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to upload file");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(song);
    }

    @PostMapping("/{songId}/like")
    public ResponseEntity<?> likeSong(@PathVariable("songId") long songId) {
        try {
            final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            this.songService.likeSong(user, songId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{songId}/like")
    public ResponseEntity<?> unlikeSong(@PathVariable("songId") long songId) {
        try {
            final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            this.songService.unlikeSong(user, songId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
