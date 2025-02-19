package org.saartako.client.controls;

import atlantafx.base.util.Animations;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.saartako.client.constants.Route;
import org.saartako.client.services.RouterService;

import java.util.Map;

public class RouterSkin extends SkinBase<Router> {

    private final RouterService routerService = RouterService.getInstance();

    private final StackPane node = new StackPane();

    public RouterSkin(Router control) {
        super(control);

        final Node defaultRoute = control.getDefaultRoute();
        final Map<Route, Node> routes = control.getRoutes();

        registerChangeListener(this.routerService.currentRouteProperty(), observable ->
            onRouteChange(this.routerService.getCurrentRoute(), routes, defaultRoute));

        onRouteChange(this.routerService.getCurrentRoute(), routes, defaultRoute);

        getChildren().setAll(this.node);
    }

    private void onRouteChange(Route route, Map<Route, Node> routes, Node defaultRoute) {
        if (route == null) {
            this.node.getChildren().clear();
        } else {
            final Node routeNode = routes.getOrDefault(route, defaultRoute);

            Animations.fadeIn(routeNode, Duration.seconds(1)).playFromStart();

            this.node.getChildren().setAll(routeNode);
        }
    }
}