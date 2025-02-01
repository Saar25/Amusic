package org.saartako.client.controllers;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.controls.ToggleSwitch;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {

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

        loginButton.setOnAction(event -> {
            if (loginMethodSwitch.isSelected()) {
                System.out.println("Trying to sign in\n" +
                                   usernameField.getText() + "\n" +
                                   passwordField.getPassword());
            } else {
                System.out.println("Trying to register\n" +
                                   usernameField.getText() + "\n" +
                                   passwordField.getPassword() + "\n" +
                                   displayNameField.getText());
            }
        });
    }
}


