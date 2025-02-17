package org.saartako.server.song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SongRepository extends JpaRepository<SongEntity, Long> {

    @Query("SELECT s FROM songs s LEFT JOIN FETCH s.genre LEFT JOIN FETCH s.language LEFT JOIN FETCH s.uploader")
    List<SongEntity> findAllWithGenreAndLanguage();

}