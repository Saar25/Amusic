package org.saartako.client.controls;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.StackPane;
import org.saartako.client.constants.Route;
import org.saartako.client.services.RouterService;

import java.util.Map;

public class RouterSkin implements Skin<Router> {

    private final RouterService routerService = RouterService.getInstance();

    private final Router control;

    private final StackPane node = new StackPane();

    private final ChangeListener<Route> routeChangeListener;

    public RouterSkin(Router control) {
        this.control = control;

        routeChangeListener = (o, prev, route) -> onRouteChange(route);
        onRouteChange(this.routerService.getCurrentRoute());

        this.routerService.currentRouteProperty().addListener(routeChangeListener);
    }

    private void onRouteChange(Route route) {
        if (route == null) {
            this.node.getChildren().clear();
        } else {
            final Node defaultRoute = this.control.getDefaultRoute();
            final Map<Route, Node> routes = this.control.getRoutes();

            final Node routeNode = routes.getOrDefault(route, defaultRoute);

            this.node.getChildren().setAll(routeNode);
        }
    }

    @Override
    public Router getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
        this.routerService.currentRouteProperty().removeListener(this.routeChangeListener);
    }
}