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

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;

public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HttpService httpService;

    private final JwtParser<User> userJwtParser = new UserJwtParser();

    private final StringProperty jwtToken = new SimpleStringProperty(this, "jwtToken", null);

    private final ObjectBinding<User> loggedUser = Bindings.createObjectBinding(
        () -> this.jwtToken.getValue() == null ? null : this.userJwtParser.parse(this.jwtToken.getValue()),
        this.jwtToken
    );

    private final BooleanBinding isLoggedIn = this.jwtToken.isNotNull();

    private AuthService(HttpService httpService) {
        this.httpService = httpService;
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
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Trying to sign in {}", username);
                final String jwtToken = this.httpService.login(username, password);
                LOGGER.info("Signed in successfully - {}", jwtToken);

                this.jwtToken.set(jwtToken);

                return jwtToken;
            } catch (IOException | InterruptedException e) {
                LOGGER.info("Failed to sign - {}", e.getMessage());

                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<String> register(String username, String password, String displayName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Trying to registry {}", username);
                final String jwtToken = this.httpService.register(username, password, displayName);
                LOGGER.info("Registered successfully - {}", jwtToken);

                this.jwtToken.set(jwtToken);

                return jwtToken;
            } catch (IOException | InterruptedException e) {
                LOGGER.info("Failed to register - {}", e.getMessage());

                throw new RuntimeException(e);
            }
        });
    }

    private static final class InstanceHolder {
        private static final AuthService INSTANCE = new AuthService(HttpService.getInstance());
    }
}
