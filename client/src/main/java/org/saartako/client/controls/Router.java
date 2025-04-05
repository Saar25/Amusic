package org.saartako.client.controls;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.saartako.client.enums.Route;
import org.saartako.client.services.RouterService;

import java.util.Map;

/**
 * Router control, always showing the node of the current route
 */
public class Router extends Control {

    private final RouterService routerService = RouterService.getInstance();

    private final ObjectBinding<Node> currentRouteNode;

    public Router(Map<Route, Node> routes, Node defaultRoute) {
        this.currentRouteNode = Bindings.createObjectBinding(() -> {
            final Route currentRoute = this.routerService.currentRouteProperty().get();

            return routes.getOrDefault(currentRoute, defaultRoute);
        }, this.routerService.currentRouteProperty());
    }

    public ObjectBinding<Node> currentRouteNodeProperty() {
        return this.currentRouteNode;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RouterSkin(this);
    }
}