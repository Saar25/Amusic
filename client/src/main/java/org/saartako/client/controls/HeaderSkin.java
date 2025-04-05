package org.saartako.client.controls;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.Config;
import org.saartako.client.components.RequiredToggleButton;
import org.saartako.client.enums.AppTheme;
import org.saartako.client.enums.Route;
import org.saartako.client.utils.LayoutUtils;
import org.saartako.common.user.User;

public class HeaderSkin extends SkinBase<Header> {

    public HeaderSkin(Header control) {
        super(control);

        final Label titleLabel = new Label("Amusic");
        titleLabel.getStyleClass().add(Styles.TITLE_2);

        final Separator separator = new Separator(Orientation.VERTICAL);

        final Label welcomeLabel = createWelcomeLabel();

        final Button themeChangeButton = createThemeChangeButton();

        final Button signOutButton = createSignOutButton();

        final Node vSpace = LayoutUtils.createHorizontalSpace();

        final InputGroup tabsInputGroup = createToggleGroup();

        final ToolBar toolBar = new ToolBar(titleLabel, separator, welcomeLabel,
            themeChangeButton, signOutButton, vSpace, tabsInputGroup);
        toolBar.getStyleClass().add(Styles.ELEVATED_2);
        toolBar.setPadding(new Insets(Config.GAP_LARGE));

        final Button previousButton = new Button("Previous",
            new FontIcon(Material2AL.ARROW_BACK));
        previousButton.getStyleClass().add(Styles.FLAT);
        VBox.setMargin(previousButton, new Insets(Config.GAP_MEDIUM, 0, 0, Config.GAP_LARGE));

        previousButton.setOnAction(event -> getSkinnable().onPreviousButtonClick());
        previousButton.managedProperty().bind(getSkinnable().hasHistoryProperty());
        previousButton.visibleProperty().bind(getSkinnable().hasHistoryProperty());

        getChildren().setAll(new VBox(toolBar, previousButton));
    }

    private Label createWelcomeLabel() {
        final Label welcomeLabel = new Label();
        registerChangeListener(getSkinnable().loggedUserProperty(), observable -> {
            final User user = getSkinnable().loggedUserProperty().get();
            final String welcomeMessage = user == null
                ? "Unidentified user"
                : "Welcome " + user.getDisplayName();
            Platform.runLater(() -> welcomeLabel.textProperty().set(welcomeMessage));
        });
        welcomeLabel.managedProperty().bind(getSkinnable().isLoggedInProperty());
        welcomeLabel.visibleProperty().bind(getSkinnable().isLoggedInProperty());
        return welcomeLabel;
    }

    private Button createThemeChangeButton() {
        final FontIcon themeChangeButtonGraphic = new FontIcon(Material2MZ.WB_SUNNY);
        themeChangeButtonGraphic.setIconSize(14);
        final Button themeChangeButton = new Button(null, themeChangeButtonGraphic);
        themeChangeButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        themeChangeButton.setOnAction(event -> {
            switch (getSkinnable().appThemeProperty().get()) {
                case LIGHT -> getSkinnable().appThemeProperty().set(AppTheme.DARK);
                case DARK -> getSkinnable().appThemeProperty().set(AppTheme.LIGHT);
            }
        });
        return themeChangeButton;
    }

    private Button createSignOutButton() {
        final Button signOutButton = new Button("Sign out");
        signOutButton.getStyleClass().addAll(Styles.ACCENT, Styles.FLAT);
        signOutButton.setOnAction(event -> getSkinnable().onSignOutButtonClick());
        signOutButton.managedProperty().bind(getSkinnable().isLoggedInProperty());
        signOutButton.visibleProperty().bind(getSkinnable().isLoggedInProperty());
        return signOutButton;
    }

    private InputGroup createToggleGroup() {
        final ToggleGroup toggleGroup = new ToggleGroup();

        final RequiredToggleButton songsToggleButton = createToggleButton(Route.SONGS, toggleGroup);
        final RequiredToggleButton myPlaylistsToggleButton = createToggleButton(Route.MY_PLAYLISTS, toggleGroup);
        final RequiredToggleButton uploadToggleButton = createToggleButton(Route.UPLOAD, toggleGroup);
        final InputGroup tabsInputGroup = new InputGroup(
            songsToggleButton, myPlaylistsToggleButton, uploadToggleButton);
        tabsInputGroup.managedProperty().bind(getSkinnable().isLoggedInProperty());
        tabsInputGroup.visibleProperty().bind(getSkinnable().isLoggedInProperty());

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            final Route newRoute = (Route) newValue.getUserData();
            getSkinnable().onSelectedRouteChange(newRoute);
        });

        registerChangeListener(getSkinnable().currentRouteProperty(), observable -> {
            switch (getSkinnable().currentRouteProperty().get()) {
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
}
