package org.saartako.common.playlist;

public class PlaylistUtils {

    private PlaylistUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
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
