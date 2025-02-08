package org.saartako.client.services;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.saartako.user.User;

public class UserService {

    private final ObjectProperty<User> loggedUser = new SimpleObjectProperty<>(this, "loggedUser", null);

    private final BooleanBinding isLoggedIn = this.loggedUser.isNotNull();

    private UserService() {
    }

    public static UserService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ObjectProperty<User> loggedUserProperty() {
        return this.loggedUser;
    }

    public User getLoggedUser() {
        return this.loggedUser.getValue();
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser.setValue(loggedUser);
    }

    public BooleanBinding isLoggedInProperty() {
        return this.isLoggedIn;
    }

    public Boolean isLoggedIn() {
        return this.isLoggedIn.get();
    }

    private static final class InstanceHolder {
        private static final UserService INSTANCE = new UserService();
    }
}
