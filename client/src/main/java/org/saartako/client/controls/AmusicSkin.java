package org.saartako.client.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import org.saartako.client.enums.Route;

import java.util.Map;

public class AmusicSkin extends SkinBase<Amusic> {

    public AmusicSkin(Amusic control) {
        super(control);

        try {
            final Map<Route, Node> routeObjectMap = Map.ofEntries(
                Map.entry(Route.LOGIN, new FXMLLoader(getClass().getResource("/views/login.fxml")).load()),
                Map.entry(Route.SONGS, new SongsPage()),
                Map.entry(Route.SONG_VIEW, new SongView()),
                Map.entry(Route.PLAYLIST_VIEW, new PlaylistView()),
                Map.entry(Route.MY_PLAYLISTS, new PlaylistsPage()),
                Map.entry(Route.UPLOAD, new UploadSongPage())
            );
            final Node defaultRoute = new Label("Page not found");
            defaultRoute.getStyleClass().addAll("title-big-1", "danger");

            final Router center = new Router(routeObjectMap, defaultRoute);
            final Header top = new Header();

            final BorderPane node = new BorderPane();
            node.setCenter(center);
            node.setTop(top);

            getChildren().setAll(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
