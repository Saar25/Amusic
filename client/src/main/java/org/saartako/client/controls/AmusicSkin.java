package org.saartako.client.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.BorderPane;
import org.saartako.client.constants.Route;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.ThemeService;
import org.saartako.client.services.UserService;

import java.util.Map;

public class AmusicSkin implements Skin<Amusic> {

    private final UserService userService = UserService.getInstance();
    private final ThemeService themeService = ThemeService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final Amusic control;

    private final BorderPane node = new BorderPane();

    public AmusicSkin(Amusic control) {
        this.control = control;

        try {
            final Map<Route, Node> routeObjectMap = Map.ofEntries(
                Map.entry(Route.LOGIN, new FXMLLoader(getClass().getResource("/views/login.fxml")).load()),
                Map.entry(Route.SONGS, new FXMLLoader(getClass().getResource("/views/song-page.fxml")).load())
            );
            final Node defaultRoute = new FXMLLoader(getClass().getResource("/views/not-found.fxml")).load();

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
