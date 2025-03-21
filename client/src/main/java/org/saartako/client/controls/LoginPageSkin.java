package org.saartako.client.controls;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.saartako.client.Config;

public class LoginPageSkin extends SkinBase<LoginPage> {

    public LoginPageSkin(LoginPage control) {
        super(control);

        final Label titleLabel = new Label("Amusic");
        titleLabel.getStyleClass().addAll("title-big-1", Styles.TEXT_BOLD);

        final GridPane gridPane = createGridPane();

        gridPane.add(new Label("Username:"), 0, 0);
        final TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Value");
        gridPane.add(usernameTextField, 1, 0);

        gridPane.add(new Label("Password:"), 0, 1);
        final PasswordTextField passwordTextField = new PasswordTextField();
        passwordTextField.setPromptText("Value");
        gridPane.add(passwordTextField, 1, 1);

        final Label displayNameLabel = new Label("Display Name:");
        gridPane.add(displayNameLabel, 0, 2);
        final TextField displayNameTextField = new TextField();
        displayNameTextField.promptTextProperty().bind(usernameTextField.textProperty());
        gridPane.add(displayNameTextField, 1, 2);

        final Label loginMethodLabel = new Label("Login");
        final ToggleSwitch loginMethodToggle = new ToggleSwitch();
        loginMethodToggle.setSelected(true);

        final VBox loginVBox = new VBox(Config.GAP_MEDIUM);
        loginVBox.setMaxHeight(Double.POSITIVE_INFINITY);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.getChildren().setAll(loginMethodLabel, loginMethodToggle);
        gridPane.add(loginVBox, 2, 0, 1, 3);

        final Button loginButton = new Button();
        loginButton.setMaxWidth(Double.POSITIVE_INFINITY);
        loginButton.getStyleClass().addAll(Styles.ACCENT);
        gridPane.add(loginButton, 0, 3, 2, 1);

        loginButton.setOnAction(event -> {
            if (loginMethodToggle.isSelected()) {
                getSkinnable().login(
                    usernameTextField.getText(),
                    passwordTextField.getPassword());
            } else {
                getSkinnable().register(
                    usernameTextField.getText(),
                    passwordTextField.getPassword(),
                    displayNameTextField.getText());
            }
        });

        loginMethodLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            final boolean isSignIn = loginMethodToggle.selectedProperty().get();

            return isSignIn ? "Register" : "Sign in";
        }, loginMethodToggle.selectedProperty()));

        loginButton.textProperty().bind(Bindings.createStringBinding(() -> {
            final boolean isSignIn = loginMethodToggle.selectedProperty().get();

            return isSignIn ? "Sign in" : "Register";
        }, loginMethodToggle.selectedProperty()));

        displayNameLabel.visibleProperty().bind(loginMethodToggle.selectedProperty().not());
        displayNameLabel.managedProperty().bind(loginMethodToggle.selectedProperty().not());
        displayNameTextField.visibleProperty().bind(loginMethodToggle.selectedProperty().not());
        displayNameTextField.managedProperty().bind(loginMethodToggle.selectedProperty().not());

        final HBox hBox = new HBox();
        hBox.getChildren().setAll(gridPane);
        hBox.setAlignment(Pos.CENTER);

        final VBox vBox = new VBox(Config.GAP_LARGE);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().setAll(titleLabel, hBox);

        getChildren().setAll(vBox);
    }

    private static GridPane createGridPane() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(Config.GAP_MEDIUM);
        gridPane.setVgap(Config.GAP_MEDIUM);
        gridPane.setAlignment(Pos.CENTER);

        final ColumnConstraints cc = new ColumnConstraints();
        gridPane.getColumnConstraints().addAll(cc, cc, cc);

        final RowConstraints rr = new RowConstraints();
        gridPane.getRowConstraints().addAll(rr, rr, rr, rr);

        return gridPane;
    }
}