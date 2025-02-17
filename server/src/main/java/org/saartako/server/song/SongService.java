package org.saartako.server.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public List<SongEntity> findAll() {
        return this.songRepository.findAllWithGenreAndLanguage();
    }
}