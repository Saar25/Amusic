package org.saartako.server.song;

import org.saartako.common.genre.GenreDTO;
import org.saartako.common.language.LanguageDTO;
import org.saartako.common.song.Song;
import org.saartako.common.song.SongDTO;
import org.saartako.common.user.UserDTO;
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

        final List<? extends Song> body = songEntities.stream().map(songEntity ->
            new SongDTO()
                .setId(songEntity.getId())
                .setName(songEntity.getName())
                .setFileName(songEntity.getFileName())
                .setUploader(new UserDTO()
                    .setId(songEntity.getUploader().getId())
                    .setDisplayName(songEntity.getUploader().getDisplayName())
                )
                .setGenre(songEntity.getGenre() == null ? null :
                    new GenreDTO()
                        .setId(songEntity.getGenre().getId())
                        .setName(songEntity.getGenre().getName())
                )
                .setLanguage(songEntity.getLanguage() == null ? null : new LanguageDTO()
                    .setId(songEntity.getLanguage().getId())
                    .setName(songEntity.getLanguage().getName())
                )
        ).toList();

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") long id) {
        this.songService.deleteSong(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
