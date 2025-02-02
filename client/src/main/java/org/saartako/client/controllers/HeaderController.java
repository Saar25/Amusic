package org.saartako.client.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
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
    private ToggleGroup currentTabToggleGroup;

    @FXML
    private Toggle songsToggle;

    @FXML
    private Toggle myPlaylistsToggle;

    @FXML
    private Toggle uploadToggle;

    @FXML
    private void initialize() {
        this.songsToggle.setUserData(Route.SONGS);
        this.myPlaylistsToggle.setUserData(Route.MY_PLAYLISTS);
        this.uploadToggle.setUserData(Route.UPLOAD);

        this.routerService.currentRouteProperty().addListener((p, prev, route) -> {
            switch (route) {
                case SONGS -> this.currentTabToggleGroup.selectToggle(this.songsToggle);
                case MY_PLAYLISTS -> this.currentTabToggleGroup.selectToggle(this.myPlaylistsToggle);
                case UPLOAD -> this.currentTabToggleGroup.selectToggle(this.uploadToggle);
            }
        });
        this.currentTabToggleGroup.selectedToggleProperty().addListener((o, prev, selected) -> {
            final Route route = (Route) selected.getUserData();
            this.routerService.setCurrentRoute(route);
        });

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