package org.saartako.client.services;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.saartako.client.constants.Route;

public class RouterService {

    private final ListProperty<Route> history = new SimpleListProperty<>(
        this, "history", FXCollections.observableArrayList(Route.LOGIN));

    private final ObjectBinding<Route> currentRoute = Bindings.createObjectBinding(
        () -> this.history.get(this.history.size() - 1), this.history);

    private final BooleanBinding hasHistory = Bindings.createBooleanBinding(
        () -> this.history.size() > 1, this.history);

    private RouterService() {
    }

    public static RouterService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ObjectBinding<Route> currentRouteProperty() {
        return this.currentRoute;
    }

    public Route getCurrentRoute() {
        return this.currentRoute.getValue();
    }

    public BooleanBinding hasHistoryProperty() {
        return this.hasHistory;
    }

    public boolean hasHistory() {
        return this.hasHistory.get();
    }

    public void navigate(Route route) {
        this.history.setAll(route);
    }

    public void push(Route route) {
        this.history.add(route);
    }

    public void previous() {
        this.history.remove(this.history.size() - 1);
    }

    private static final class InstanceHolder {
        private static final RouterService INSTANCE = new RouterService();
    }
}
