package org.saartako.client.services;

import org.saartako.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;

public class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static LoginService INSTANCE;

    private final HttpService httpService;
    private final UserService userService;

    public LoginService(HttpService httpService, UserService userService) {
        this.httpService = httpService;
        this.userService = userService;
    }

    public static synchronized LoginService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoginService(HttpService.getInstance(), UserService.getInstance());
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
                LOGGER.error("Failed to sign in", e);

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
                LOGGER.error("Failed to register", e);

                throw new RuntimeException(e);
            }
        });
    }
}
