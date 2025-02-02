package org.saartako.client.components;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.saartako.client.constants.Route;
import org.saartako.client.services.RouterService;

import java.util.Map;

public class Router extends StackPane {

    private final RouterService routerService = RouterService.getInstance();

    public Router(Map<Route, Node> routes, Node defaultRoute) {
        this.routerService.currentRouteProperty().addListener((o, prev, route) -> {

            final Node routeNode = routes.getOrDefault(route, defaultRoute);

            if (routeNode != null) {
                getChildren().setAll(routeNode);
            } else {
                getChildren().clear();
            }
        });
    }

}