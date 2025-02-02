package org.saartako.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public List<SongEntity> findAll() {
        return this.songRepository.findAll();
    }

    public Optional<SongEntity> findById(long id) {
        return this.songRepository.findById(id);
    }
}