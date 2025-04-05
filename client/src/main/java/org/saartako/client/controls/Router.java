package org.saartako.client.controls;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.saartako.client.enums.Route;

import java.util.Map;

/**
 * Router control, always showing the node of the current route
 */
public class Router extends Control {

    private final Map<Route, Node> routes;
    private final Node defaultRoute;

    public Router(Map<Route, Node> routes, Node defaultRoute) {
        this.routes = routes;
        this.defaultRoute = defaultRoute;
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