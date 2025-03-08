package org.saartako.server.genre;

import org.saartako.common.genre.Genre;
import org.saartako.common.genre.GenreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/genre")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping("")
    public ResponseEntity<List<? extends Genre>> findAll() {
        final List<GenreEntity> genreEntities = this.genreService.findAll();

        final List<? extends Genre> body = GenreUtils.copy(genreEntities);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
