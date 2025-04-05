package org.saartako.client.controls;

import javafx.scene.Node;
import javafx.scene.control.Control;
import org.saartako.client.enums.Route;

import java.util.Map;

/**
 * Root node for amusic application, showing the header and the router as body
 */
public class Amusic extends Control {

    private final Map<Route, Node> routes = Map.ofEntries(
        Map.entry(Route.LOGIN, new LoginPage()),
        Map.entry(Route.SONGS, new SongsPage()),
        Map.entry(Route.SONG_VIEW, new SongView()),
        Map.entry(Route.PLAYLIST_VIEW, new PlaylistView()),
        Map.entry(Route.MY_PLAYLISTS, new PlaylistsPage()),
        Map.entry(Route.UPLOAD, new UploadSongPage())
    );

    @Override
    protected AmusicSkin createDefaultSkin() {
        return new AmusicSkin(this);
    }

    public Map<Route, Node> getRoutes() {
        return this.routes;
    }
}
