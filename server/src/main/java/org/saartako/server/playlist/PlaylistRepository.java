package org.saartako.server.playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {

    @Query("""
        SELECT p FROM playlists p
        LEFT JOIN FETCH p.songIds
        LEFT JOIN FETCH p.owner
        WHERE p.owner.id = :ownerId OR p.isPrivate IS NOT TRUE
        """)
    List<PlaylistEntity> findPublicOrByOwnerId(@Param("ownerId") long ownerId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (:playlistId, :songId)", nativeQuery = true)
    void addPlaylistSong(@Param("playlistId") long playlistId, @Param("songId") long songId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlist_songs ps WHERE ps.playlist_id = :playlistId AND ps.song_id = :songId", nativeQuery = true)
    void deletePlaylistSong(@Param("playlistId") Long playlistId, @Param("songId") Long songId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlist_songs ps WHERE ps.song_id = :songId", nativeQuery = true)
    void deleteSongFromPlaylists(@Param("songId") Long songId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlist_songs ps WHERE ps.playlist_id = :playlistId", nativeQuery = true)
    void deletePlaylistFromSongs(@Param("playlistId") Long playlistId);
}