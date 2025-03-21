package org.saartako.common.playlist;

import org.saartako.common.song.Song;
import org.saartako.common.song.SongDTO;
import org.saartako.common.song.SongUtils;
import org.saartako.common.user.UserUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PlaylistUtils {

    private PlaylistUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static List<PlaylistDTO> copyDisplay(List<? extends Playlist> playlists) {
        return playlists.stream().map(PlaylistUtils::copyDisplay).toList();
    }

    public static PlaylistDTO copyDisplay(Playlist playlist) {
        return playlist == null
            ? null
            : new PlaylistDTO()
                .setId(playlist.getId())
                .setOwner(UserUtils.copyDisplay(playlist.getOwner()))
                .setName(playlist.getName())
                .setPrivate(playlist.isPrivate())
                .setModifiable(playlist.isModifiable())
                .setSongIds(playlist.getSongIds() == null ? Set.of() : Set.copyOf(playlist.getSongIds()))
                .setSongs(playlist.getSongs() == null ? Set.of() : SongUtils.copyDisplay(playlist.getSongs()));
    }

    public static PlaylistDTO mergeSongs(Playlist playlist, List<Song> songs) {
        if (playlist == null) {
            return null;
        }

        final Set<SongDTO> playlistSongs = playlist.getSongIds() == null
            ? Set.of()
            : playlist.getSongIds().stream()
                .map(id ->
                    songs.stream()
                        .filter(s -> s.getId() == id)
                        .findAny()
                        .map(SongUtils::copyDisplay)
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return copyDisplay(playlist).setSongs(playlistSongs);
    }

    public static String toString(Playlist playlist) {
        return "Playlist{" +
               "id=" + playlist.getId() +
               ", owner=" + playlist.getOwner() +
               ", name='" + playlist.getName() + '\'' +
               ", isPrivate=" + playlist.isPrivate() +
               ", isModifiable=" + playlist.isModifiable() +
               ", songs=" + playlist.getSongs() +
               '}';
    }
}
