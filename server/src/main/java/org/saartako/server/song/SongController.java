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

    @PostMapping("")
    public ResponseEntity<?> createSong(@RequestBody CreateSongDTO songDTO) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final SongEntity song = this.songService.createSong(
            user, songDTO.name(), songDTO.genreId(), songDTO.languageId());

        return ResponseEntity.status(HttpStatus.CREATED).body(song);
    }

    @PostMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadSong(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            this.songService.uploadSong(user, id, file);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to save file");
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeSong(@PathVariable("id") long id) {
        try {
            final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            this.songService.likeSong(user, id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
