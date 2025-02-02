package org.saartako.client.controllers;

import atlantafx.base.theme.Styles;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.saartako.client.constants.Route;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.UserService;

public class HeaderController {

    private final UserService userService = UserService.getInstance();

    private final RouterService routerService = RouterService.getInstance();

    @FXML
    private Label welcomeText;

    @FXML
    private ToggleGroup currentTab;

    @FXML
    private ToggleButton songsToggleButton;

    @FXML
    private ToggleButton myPlaylistsToggleButton;

    @FXML
    private ToggleButton uploadToggleButton;

    @FXML
    private void initialize() {
        this.currentTab.selectedToggleProperty().addListener((o, prev, selected) -> System.out.println(selected));

        this.welcomeText.textProperty().bind(Bindings.createObjectBinding(
            () -> this.userService.getLoggedUser() == null
                ? null
                : "Welcome " + this.userService.getLoggedUser().getDisplayName(),
            this.userService.loggedUserProperty()));
    }

    public void onSignOutAction() {
        this.userService.setLoggedUser(null);
        this.routerService.setCurrentRoute(Route.LOGIN);
    }
}