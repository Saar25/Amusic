package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.saartako.client.enums.Route;
import org.saartako.client.services.AuthService;
import org.saartako.client.services.RouterService;

/**
 * Login page, showing login and register forms, and navigating the user into the application
 */
public class LoginPage extends Control {

    private final RouterService routerService = RouterService.getInstance();
    private final AuthService authService = AuthService.getInstance();

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LoginPageSkin(this);
    }

    public void login(String username, String password) {
        this.authService.login(username, password).whenComplete((jwtToken, error) -> {
            if (jwtToken != null) {
                Platform.runLater(() -> this.routerService.navigate(Route.SONGS));
            }
            if (error != null) {
                Platform.runLater(() -> {
                    final Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Failed to sign in\n" + error.getMessage());
                    alert.show();
                });
            }
        });
    }

    public void register(String username, String password, String displayName) {
        final String displayNameSafe = displayName.isEmpty() ? username : displayName;

        this.authService.register(username, password, displayNameSafe).whenComplete((jwtToken, error) -> {
            if (jwtToken != null) {
                Platform.runLater(() -> this.routerService.navigate(Route.SONGS));
            }
            if (error != null) {
                Platform.runLater(() -> {
                    final Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Failed to register\n" + error.getMessage());
                    alert.show();
                });
            }
        });
    }
}