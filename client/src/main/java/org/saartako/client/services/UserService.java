package org.saartako.client.services;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.saartako.encrypt.JwtParser;
import org.saartako.encrypt.UserJwtParser;
import org.saartako.user.User;

public class UserService {

    private final JwtParser<User> userJwtParser = new UserJwtParser();

    private final StringProperty jwtToken = new SimpleStringProperty(this, "jwtToken", null);

    private final ObjectBinding<User> loggedUser = Bindings.createObjectBinding(
        () -> this.jwtToken.getValue() == null ? null : this.userJwtParser.parse(this.jwtToken.getValue()),
        this.jwtToken
    );

    private final BooleanBinding isLoggedIn = this.jwtToken.isNotNull();

    private UserService() {
    }

    public static UserService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public StringProperty jwtTokenProperty() {
        return this.jwtToken;
    }

    public String getJwtToken() {
        return this.jwtToken.getValue();
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken.setValue(jwtToken);
    }

    public ObjectBinding<User> loggedUserProperty() {
        return this.loggedUser;
    }

    public User getLoggedUser() {
        return this.loggedUser.getValue();
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
