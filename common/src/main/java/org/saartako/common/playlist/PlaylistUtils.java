package org.saartako.common.playlist;

import org.saartako.common.user.UserUtils;

import java.util.List;
import java.util.Set;

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
                .setSongIds(Set.copyOf(playlist.getSongIds()));
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
