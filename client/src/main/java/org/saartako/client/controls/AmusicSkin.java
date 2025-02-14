package org.saartako.client.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.BorderPane;
import org.saartako.client.constants.Route;

import java.util.Map;

public class AmusicSkin implements Skin<Amusic> {

    private final Amusic control;

    private final BorderPane node = new BorderPane();

    public AmusicSkin(Amusic control) {
        this.control = control;

        try {
            final Map<Route, Node> routeObjectMap = Map.ofEntries(
                Map.entry(Route.LOGIN, new FXMLLoader(getClass().getResource("/views/login.fxml")).load()),
                Map.entry(Route.SONGS, new SongsPage()),
                Map.entry(Route.MY_PLAYLISTS, new PlaylistsPage())
            );
            final Node defaultRoute = new Label("Page not found");
            defaultRoute.getStyleClass().addAll("title-big-1", "danger");

            final Router center = new Router(routeObjectMap, defaultRoute);
            final Header top = new Header();

            this.node.setCenter(center);
            this.node.setTop(top);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Amusic getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
    }
}
