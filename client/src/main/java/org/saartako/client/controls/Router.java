package org.saartako.client.controls;

import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.saartako.client.constants.Route;

public class Router extends Control {

    private final MapProperty<Route, Node> routes = new SimpleMapProperty<>(this, "routes");
    private final ObjectProperty<Node> defaultRoute = new SimpleObjectProperty<>(this, "defaultRoute");

    public MapProperty<Route, Node> routesProperty() {
        return this.routes;
    }

    public ObservableMap<Route, Node> getRoutes() {
        return this.routes.get();
    }

    public ObjectProperty<Node> defaultRouteProperty() {
        return this.defaultRoute;
    }

    public Node getDefaultRoute() {
        return this.defaultRoute.get();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RouterSkin(this);
    }
}