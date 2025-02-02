package org.saartako.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.saartako.client.components.Router;
import org.saartako.client.constants.Route;
import org.saartako.client.services.RouterService;

import java.io.IOException;
import java.util.Map;

public class RouterProvider {

    public Router createRouter() throws IOException {
        final Map<Route, Node> routes = Map.ofEntries(
            Map.entry(Route.LOGIN, new FXMLLoader(getClass().getResource("/views/login.fxml")).load()),
            Map.entry(Route.SONGS, new FXMLLoader(getClass().getResource("/views/song-page.fxml")).load())
        );
        final Node defaultRoute = new FXMLLoader(getClass().getResource("/views/not-found.fxml")).load();
        final Router router = new Router(routes, defaultRoute);

        final RouterService routerService = RouterService.getInstance();
        routerService.setCurrentRoute(Route.SONGS);

        return router;
    }

}
