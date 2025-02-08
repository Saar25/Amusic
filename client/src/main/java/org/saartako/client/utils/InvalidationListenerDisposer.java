package org.saartako.client.utils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class InvalidationListenerDisposer {

    private final List<Pair<Observable, InvalidationListener>> listeners = new ArrayList<>();

    public void listen(Observable observable, InvalidationListener listener) {
        observable.addListener(listener);

        this.listeners.add(new Pair<>(observable, listener));
    }

    public void dispose() {
        for (Pair<Observable, InvalidationListener> pair : this.listeners) {
            final Observable observable = pair.getKey();
            final InvalidationListener listener = pair.getValue();
            observable.removeListener(listener);
        }
    }
}
