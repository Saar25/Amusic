package org.saartako.client.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.saartako.user.User;

public class UserService {

    private final ObjectProperty<User> loggedUserProperty = new SimpleObjectProperty<>(this, "loggedUser", null);

    private UserService() {
    }

    public static UserService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ObjectProperty<User> loggedUserProperty() {
        return this.loggedUserProperty;
    }

    public User getLoggedUser() {
        return this.loggedUserProperty.getValue();
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUserProperty.setValue(loggedUser);
    }

    private static final class InstanceHolder {
        private static final UserService INSTANCE = new UserService();
    }
}
