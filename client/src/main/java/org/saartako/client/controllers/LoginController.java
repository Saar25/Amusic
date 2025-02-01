package org.saartako.client.controllers;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.controls.ToggleSwitch;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.saartako.client.services.HttpService;
import org.saartako.user.User;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class LoginController {

    private final HttpService httpService = HttpService.getInstance();

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
                System.out.println("Trying to sign in...");

                CompletableFuture.runAsync(() -> {
                    try {
                        final User user = httpService.login(username, password);

                        System.out.println("Signed in successfully, redirecting to songs page\n" + user);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                final String username = usernameField.getText();
                final String password = passwordField.getPassword();
                final String displayName = displayNameField.getText().isEmpty() ? username : displayNameField.getText();
                System.out.println("Trying to registry...");

                CompletableFuture.runAsync(() -> {
                    try {
                        final User user = httpService.register(username, password, displayName);

                        System.out.println("Registered successfully, redirecting to songs page\n" + user);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }
}


