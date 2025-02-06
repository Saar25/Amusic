package org.saartako.client.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.saartako.client.constants.Route;

import java.io.IOException;
import java.util.Map;

public class Router extends Control {

    private final Map<Route, Node> routes = Map.ofEntries(
        Map.entry(Route.LOGIN, new FXMLLoader(getClass().getResource("/views/login.fxml")).load()),
        Map.entry(Route.SONGS, new FXMLLoader(getClass().getResource("/views/song-page.fxml")).load())
    );
    private final Node defaultRoute = new FXMLLoader(getClass().getResource("/views/not-found.fxml")).load();

    public Router() throws IOException {

    }

    public Map<Route, Node> getRoutes() {
        return this.routes;
    }

    public Node getDefaultRoute() {
        return this.defaultRoute;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RouterSkin(this);
    }
}