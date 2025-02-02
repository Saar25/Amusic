package org.saartako.client.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.saartako.client.constants.Route;

public class RouterService {

    private static RouterService INSTANCE;

    private final ObjectProperty<Route> currentRouteProperty = new SimpleObjectProperty<>();

    private RouterService() {
    }

    public static synchronized RouterService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RouterService();
        }
        return INSTANCE;
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
}
