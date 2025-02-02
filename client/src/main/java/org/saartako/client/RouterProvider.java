package org.saartako.client;

import javafx.fxml.FXMLLoader;
import org.saartako.client.components.Router;
import org.saartako.client.constants.Route;
import org.saartako.client.services.RouterService;

import java.io.IOException;
import java.util.Map;

public class RouterProvider {

    public Router createRouter() throws IOException {
        final Router router = new Router(Map.ofEntries(
            Map.entry(Route.LOGIN, new FXMLLoader(getClass().getResource("/views/login.fxml")).load()),
            Map.entry(Route.SONGS, new FXMLLoader(getClass().getResource("/views/songs.fxml")).load()),
            Map.entry(Route.TEST, new FXMLLoader(getClass().getResource("/views/test.fxml")).load())
        ));

        final RouterService routerService = RouterService.getInstance();
        routerService.setCurrentRoute(Route.SONGS);

        return router;
    }

}
