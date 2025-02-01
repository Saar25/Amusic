package org.saartako.client.services;

import org.saartako.user.User;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class LoginService {

    private static LoginService INSTANCE;

    private final HttpService httpService;

    private LoginService(HttpService httpService) {
        this.httpService = httpService;
    }

    public static synchronized LoginService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoginService(HttpService.getInstance());
        }
        return INSTANCE;
    }

    public CompletableFuture<User> login(String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Trying to sign in...");

                final User user = this.httpService.login(username, password);

                System.out.println("Signed in successfully, redirecting to songs page\n" + user);

                return user;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<User> register(String username, String password, String displayName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Trying to registry...");

                final User user = this.httpService.register(username, password, displayName);

                System.out.println("Registered successfully, redirecting to songs page\n" + user);

                return user;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
