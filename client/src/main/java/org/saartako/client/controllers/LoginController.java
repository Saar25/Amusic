package org.saartako.client.controllers;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.controls.ToggleSwitch;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.saartako.client.constants.Route;
import org.saartako.client.services.AuthService;
import org.saartako.client.services.RouterService;

public class LoginController {

    private final RouterService routerService = RouterService.getInstance();
    private final AuthService authService = AuthService.getInstance();

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordTextField passwordField;

    @FXML
    private TextField displayNameField;

    @FXML
    private Label displayNameLabel;

    @FXML
    private Label loginMethodLabel;

    @FXML
    private ToggleSwitch loginMethodSwitch;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize() {
        this.loginButton.setText("Sign in");
        this.loginMethodLabel.setText("Sign in");

        this.loginMethodSwitch.selectedProperty().addListener((o, prev, isSignIn) -> {
            final String method = isSignIn ? "Sign in" : "Register";
            this.loginMethodLabel.setText(method);
            this.loginButton.setText(method);

            this.displayNameLabel.setVisible(!isSignIn);
            this.displayNameLabel.setManaged(!isSignIn);
            this.displayNameField.setVisible(!isSignIn);
            this.displayNameField.setManaged(!isSignIn);
        });

        this.displayNameField.promptTextProperty().bind(this.usernameField.textProperty());

        this.loginButton.setOnAction(event -> {
            if (this.loginMethodSwitch.isSelected()) {
                login();
            } else {
                register();
            }
        });
    }

    private void login() {
        final String username = usernameField.getText();
        final String password = passwordField.getPassword();

        this.authService.login(username, password).whenComplete((user, error) -> {
            if (user != null) {
                Platform.runLater(() -> this.routerService.setCurrentRoute(Route.SONGS));
            }
            if (error != null) {
                Platform.runLater(() -> {
                    final Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Failed to sign in\n" + error);
                    alert.showAndWait();
                });
            }
        });
    }

    private void register() {
        final String username = this.usernameField.getText();
        final String password = this.passwordField.getPassword();
        final String displayName = this.displayNameField.getText().isEmpty() ? username : this.displayNameField.getText();

        this.authService.register(username, password, displayName).whenComplete((user, error) -> {
            if (user != null) {
                this.routerService.setCurrentRoute(Route.SONGS);
            }
            if (error != null) {
                Platform.runLater(() -> {
                    final Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Failed to register\n" + error);
                    alert.showAndWait();
                });
            }
        });
    }
}