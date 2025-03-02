package org.saartako.server.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    @Query("SELECT COUNT(sl) FROM likes sl WHERE sl.song.id = :songId")
    long findSongLikeCount(@Param("songId") Long songId);

    @Query("SELECT sl.song.id FROM likes sl WHERE sl.user.id = :userId")
    List<Long> findLikedSongIdsByUserId(@Param("userId") Long userId);

}