package org.saartako.client.services;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.saartako.common.encrypt.JwtParser;
import org.saartako.common.encrypt.UserJwtParser;
import org.saartako.common.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;

public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AuthApiService authApiService;

    private final JwtParser<User> userJwtParser = new UserJwtParser();

    private final StringProperty jwtToken = new SimpleStringProperty(this, "jwtToken", null);

    private final ObjectBinding<User> loggedUser = Bindings.createObjectBinding(
        () -> this.jwtToken.getValue() == null ? null : this.userJwtParser.parse(this.jwtToken.getValue()),
        this.jwtToken
    );

    private final BooleanBinding isLoggedIn = this.loggedUser.isNotNull();

    private AuthService(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }

    public static AuthService getInstance() {
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

    public CompletableFuture<String> login(String username, String password) {
        LOGGER.info("Trying to sign in {}", username);

        return this.authApiService.login(username, password)
            .whenComplete((jwtToken, throwable) -> {
                if (throwable != null) {
                    LOGGER.info("Failed to sign in - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to sign in - {}", jwtToken);

                    this.jwtToken.set(jwtToken);
                }
            });
    }

    public CompletableFuture<String> register(String username, String password, String displayName) {
        LOGGER.info("Trying to register {}", username);

        return this.authApiService.register(username, password, displayName)
            .whenComplete((jwtToken, throwable) -> {
                if (throwable != null) {
                    LOGGER.info("Failed to register - {}", throwable.getMessage());
                } else {
                    LOGGER.info("Succeeded to register - {}", jwtToken);

                    this.jwtToken.set(jwtToken);
                }
            });
    }

    public CompletableFuture<String> requireJwtToken() {
        return isLoggedIn()
            ? CompletableFuture.completedFuture(getJwtToken())
            : CompletableFuture.failedFuture(new NullPointerException("User is not logged in"));
    }

    private static final class InstanceHolder {
        private static final AuthService INSTANCE = new AuthService(
            AuthApiService.getInstance()
        );
    }
}
