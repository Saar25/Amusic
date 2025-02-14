package org.saartako.playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {

    @Query("SELECT p FROM playlists p WHERE p.owner.id = :ownerId")
    List<PlaylistEntity> findByOwnerId(@Param("ownerId") String ownerId);

}