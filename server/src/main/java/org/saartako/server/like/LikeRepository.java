package org.saartako.server.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    @Query("SELECT COUNT(sl) FROM likes sl WHERE sl.song.id = :songId")
    long findSongLikeCount(@Param("songId") Long songId);

    @Query("SELECT sl.song.id FROM likes sl WHERE sl.user.id = :userId")
    List<Long> findLikedSongIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM likes sl WHERE sl.user.id = :userId AND sl.song.id = :songId")
    void unlikeSong(@Param("userId") Long userId, @Param("songId") Long songId);

}