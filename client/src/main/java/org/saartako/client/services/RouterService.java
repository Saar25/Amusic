package org.saartako.client.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.saartako.client.constants.Route;

public class RouterService {

    private final ObjectProperty<Route> currentRouteProperty = new SimpleObjectProperty<>(Route.LOGIN);

    private RouterService() {
    }

    public static RouterService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ObjectProperty<Route> currentRouteProperty() {
        return this.currentRouteProperty;
    }

    public Route getCurrentRoute() {
        return this.currentRouteProperty.getValue();
    }

    public void setCurrentRoute(Route route) {
        this.currentRouteProperty.setValue(route);
    }

    private static final class InstanceHolder {
        private static final RouterService INSTANCE = new RouterService();
    }
}
