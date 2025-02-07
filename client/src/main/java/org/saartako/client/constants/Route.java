package org.saartako.client.constants;

public enum Route {
    LOGIN("Login"),
    SONGS("Songs"),
    SONG_VIEW("Song View"),
    MY_PLAYLISTS("My Playlists"),
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
