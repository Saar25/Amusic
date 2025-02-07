package org.saartako.client.controls;

import atlantafx.base.layout.InputGroup;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.components.RequiredToggleButton;
import org.saartako.client.constants.Route;
import org.saartako.client.enums.AppTheme;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.ThemeService;
import org.saartako.client.services.UserService;
import org.saartako.user.User;

public class HeaderSkin implements Skin<Header> {

    private final UserService userService = UserService.getInstance();
    private final ThemeService themeService = ThemeService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final Header control;

    private final ToolBar node;

    private final ChangeListener<User> userChangeListener;
    private final ChangeListener<Route> routeChangeListener;

    public HeaderSkin(Header control) {
        this.control = control;

        final BooleanBinding isLoggedIn = this.userService.loggedUserProperty().isNotNull();

        final Label titleLabel = new Label("Amusic");
        titleLabel.getStyleClass().add("title-2");

        final Separator separator = new Separator(Orientation.VERTICAL);

        final Label welcomeLabel = new Label();
        userChangeListener = (observable, oldValue, newValue) -> {
            final String welcomeMessage = this.userService.getLoggedUser() == null
                ? "Unidentified user"
                : "Welcome " + this.userService.getLoggedUser().getDisplayName();
            Platform.runLater(() -> welcomeLabel.textProperty().set(welcomeMessage));
        };
        this.userService.loggedUserProperty().addListener(userChangeListener);
        welcomeLabel.managedProperty().bind(isLoggedIn);
        welcomeLabel.visibleProperty().bind(isLoggedIn);

        final FontIcon themeChangeButtonGraphic = new FontIcon(Material2MZ.WB_SUNNY);
        themeChangeButtonGraphic.setIconSize(14);
        final Button themeChangeButton = new Button(null, themeChangeButtonGraphic);
        themeChangeButton.getStyleClass().addAll("button-icon", "flat");
        themeChangeButton.setOnAction(event -> {
            switch (this.themeService.getAppTheme()) {
                case LIGHT -> this.themeService.setAppTheme(AppTheme.DARK);
                case DARK -> this.themeService.setAppTheme(AppTheme.LIGHT);
            }
        });

        final Button signOutButton = new Button("Sign out");
        signOutButton.getStyleClass().addAll("accent", "flat");
        signOutButton.setOnAction(event -> {
            this.userService.setLoggedUser(null);
            routerService.setCurrentRoute(Route.LOGIN);
        });
        signOutButton.managedProperty().bind(isLoggedIn);
        signOutButton.visibleProperty().bind(isLoggedIn);

        final Region spacing = new Region();
        HBox.setHgrow(spacing, Priority.ALWAYS);

        final ToggleGroup toggleGroup = new ToggleGroup();

        final InputGroup tabsInputGroup = new InputGroup(
            createToggleButton(Route.SONGS, toggleGroup),
            createToggleButton(Route.MY_PLAYLISTS, toggleGroup),
            createToggleButton(Route.UPLOAD, toggleGroup)
        );
        tabsInputGroup.managedProperty().bind(isLoggedIn);
        tabsInputGroup.visibleProperty().bind(isLoggedIn);
        toggleGroup.setUserData(this.routerService.getCurrentRoute());

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            final Route newRoute = (Route) newValue.getUserData();
            this.routerService.setCurrentRoute(newRoute);
        });
        routeChangeListener = (observable, oldValue, newValue) -> toggleGroup.setUserData(newValue);
        this.routerService.currentRouteProperty().addListener(routeChangeListener);

        this.node = new ToolBar(titleLabel, separator, welcomeLabel,
            themeChangeButton, signOutButton, spacing, tabsInputGroup);
        this.node.getStyleClass().add("elevated-2");
        this.node.setPadding(new Insets(16));
    }

    private RequiredToggleButton createToggleButton(Route route, ToggleGroup toggleGroup) {
        final RequiredToggleButton toggleButton = new RequiredToggleButton();
        toggleButton.setToggleGroup(toggleGroup);
        toggleButton.setUserData(route);
        toggleButton.setText(route.toString());
        return toggleButton;
    }

    @Override
    public Header getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
        this.userService.loggedUserProperty().removeListener(this.userChangeListener);
        this.routerService.currentRouteProperty().removeListener(this.routeChangeListener);
    }
}
