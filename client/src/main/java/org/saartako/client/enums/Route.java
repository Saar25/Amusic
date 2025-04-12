package org.saartako.client.enums;

/**
 * Represents all the routes in the application
 */
public enum Route {
    LOGIN("Login"),
    SONGS("Songs"),
    SONG_VIEW("Song View"),
    MY_PLAYLISTS("My Playlists"),
    PLAYLIST_VIEW("Playlist View"),
    UPLOAD("Upload"),
    ;

    private final String name;

    Route(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
