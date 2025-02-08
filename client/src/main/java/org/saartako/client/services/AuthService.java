package org.saartako.client.services;

import org.saartako.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;

public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static AuthService INSTANCE;

    private final HttpService httpService;
    private final UserService userService;

    private AuthService(HttpService httpService, UserService userService) {
        this.httpService = httpService;
        this.userService = userService;
    }

    public static synchronized AuthService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthService(HttpService.getInstance(), UserService.getInstance());
        }
        return INSTANCE;
    }

    public CompletableFuture<User> login(String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Trying to sign in {}", username);
                final User user = this.httpService.login(username, password);
                LOGGER.info("Signed in successfully - {}", user);

                this.userService.setLoggedUser(user);

                return user;
            } catch (IOException | InterruptedException e) {
                LOGGER.info("Failed to sign - {}", e.getMessage());

                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<User> register(String username, String password, String displayName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Trying to registry {}", username);
                final User user = this.httpService.register(username, password, displayName);
                LOGGER.info("Registered successfully - {}", user);

                this.userService.setLoggedUser(user);

                return user;
            } catch (IOException | InterruptedException e) {
                LOGGER.info("Failed to register - {}", e.getMessage());

                throw new RuntimeException(e);
            }
        });
    }
}
