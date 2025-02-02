package org.saartako.client.constants;

public enum Route {
    LOGIN("login"),
    SONGS("tmp"),
    MY_PLAYLISTS("myPlaylists"),
    UPLOAD("upload"),
    ;

    private final String name;

    Route(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
