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
import org.saartako.client.services.LoginService;
import org.saartako.client.services.RouterService;

public class LoginController {

    private final RouterService routerService = RouterService.getInstance();
    private final LoginService loginService = LoginService.getInstance();

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
        loginButton.setText("Sign in");
        loginMethodLabel.setText("Sign in");

        loginMethodSwitch.selectedProperty().addListener((o, prev, isSignIn) -> {
            final String method = isSignIn ? "Sign in" : "Register";
            loginMethodLabel.setText(method);
            loginButton.setText(method);

            displayNameLabel.setVisible(!isSignIn);
            displayNameLabel.setManaged(!isSignIn);
            displayNameField.setVisible(!isSignIn);
            displayNameField.setManaged(!isSignIn);
        });

        displayNameField.promptTextProperty().bind(usernameField.textProperty());

        loginButton.setOnAction(event -> {
            if (loginMethodSwitch.isSelected()) {
                final String username = usernameField.getText();
                final String password = passwordField.getPassword();

                loginService.login(username, password).whenComplete((user, error) -> Platform.runLater(() -> {
                    final Alert alert = user != null
                        ? new Alert(Alert.AlertType.INFORMATION, "Signed in successfully\n" + user)
                        : new Alert(Alert.AlertType.INFORMATION, "Failed to sign in\n" + error);
                    alert.showAndWait();

                    if (user != null) {
                        this.routerService.setCurrentRoute(Route.TEST);
                    }
                }));
            } else {
                final String username = usernameField.getText();
                final String password = passwordField.getPassword();
                final String displayName = displayNameField.getText().isEmpty() ? username : displayNameField.getText();

                loginService.register(username, password, displayName).whenComplete((user, error) -> Platform.runLater(() -> {
                    final Alert alert = user != null
                        ? new Alert(Alert.AlertType.INFORMATION, "Registered successfully\n" + user)
                        : new Alert(Alert.AlertType.INFORMATION, "Failed to register\n" + error);
                    alert.showAndWait();

                    if (user != null) {
                        this.routerService.setCurrentRoute(Route.TEST);
                    }
                }));
            }
        });
    }
}