package org.saartako.client.controls;

import atlantafx.base.layout.InputGroup;
import javafx.application.Platform;
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
import org.saartako.client.services.AuthService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.ThemeService;
import org.saartako.client.utils.InvalidationListenerDisposer;

public class HeaderSkin implements Skin<Header> {

    private final AuthService authService = AuthService.getInstance();
    private final ThemeService themeService = ThemeService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final InvalidationListenerDisposer disposer = new InvalidationListenerDisposer();

    private final Header control;

    private final ToolBar node;

    public HeaderSkin(Header control) {
        this.control = control;

        final Label titleLabel = new Label("Amusic");
        titleLabel.getStyleClass().add("title-2");

        final Separator separator = new Separator(Orientation.VERTICAL);

        final Label welcomeLabel = createWelcomeLabel();

        final Button themeChangeButton = createThemeChangeButton();

        final Button signOutButton = createSignOutButton();

        final Region spacing = new Region();
        HBox.setHgrow(spacing, Priority.ALWAYS);

        final InputGroup tabsInputGroup = createToggleGroup();

        this.node = new ToolBar(titleLabel, separator, welcomeLabel,
            themeChangeButton, signOutButton, spacing, tabsInputGroup);
        this.node.getStyleClass().add("elevated-2");
        this.node.setPadding(new Insets(16));
    }

    private Label createWelcomeLabel() {
        final Label welcomeLabel = new Label();
        this.disposer.listen(this.authService.loggedUserProperty(), observable -> {
            final String welcomeMessage = this.authService.getLoggedUser() == null
                ? "Unidentified user"
                : "Welcome " + this.authService.getLoggedUser().getDisplayName();
            Platform.runLater(() -> welcomeLabel.textProperty().set(welcomeMessage));
        });
        welcomeLabel.managedProperty().bind(this.authService.isLoggedInProperty());
        welcomeLabel.visibleProperty().bind(this.authService.isLoggedInProperty());
        return welcomeLabel;
    }

    private Button createThemeChangeButton() {
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
        return themeChangeButton;
    }

    private Button createSignOutButton() {
        final Button signOutButton = new Button("Sign out");
        signOutButton.getStyleClass().addAll("accent", "flat");
        signOutButton.setOnAction(event -> {
            this.authService.setJwtToken(null);
            this.routerService.setCurrentRoute(Route.LOGIN);
        });
        signOutButton.managedProperty().bind(this.authService.isLoggedInProperty());
        signOutButton.visibleProperty().bind(this.authService.isLoggedInProperty());
        return signOutButton;
    }

    private InputGroup createToggleGroup() {
        final ToggleGroup toggleGroup = new ToggleGroup();

        final RequiredToggleButton songsToggleButton = createToggleButton(Route.SONGS, toggleGroup);
        final RequiredToggleButton myPlaylistsToggleButton = createToggleButton(Route.MY_PLAYLISTS, toggleGroup);
        final RequiredToggleButton uploadToggleButton = createToggleButton(Route.UPLOAD, toggleGroup);
        final InputGroup tabsInputGroup = new InputGroup(
            songsToggleButton, myPlaylistsToggleButton, uploadToggleButton);
        tabsInputGroup.managedProperty().bind(this.authService.isLoggedInProperty());
        tabsInputGroup.visibleProperty().bind(this.authService.isLoggedInProperty());

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            final Route newRoute = (Route) newValue.getUserData();
            this.routerService.setCurrentRoute(newRoute);
        });

        this.disposer.listen(this.routerService.currentRouteProperty(), observable -> {
            switch (this.routerService.getCurrentRoute()) {
                case SONGS -> toggleGroup.selectToggle(songsToggleButton);
                case MY_PLAYLISTS -> toggleGroup.selectToggle(myPlaylistsToggleButton);
                case UPLOAD -> toggleGroup.selectToggle(uploadToggleButton);
            }
        });

        return tabsInputGroup;
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
        this.disposer.dispose();
    }
}
