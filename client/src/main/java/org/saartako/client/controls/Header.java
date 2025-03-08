package org.saartako.client.controls;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Control;
import org.saartako.client.enums.AppTheme;
import org.saartako.client.enums.Route;
import org.saartako.client.services.AuthService;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.ThemeService;
import org.saartako.common.user.User;

public class Header extends Control {

    private final AuthService authService = AuthService.getInstance();
    private final ThemeService themeService = ThemeService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    @Override
    protected HeaderSkin createDefaultSkin() {
        return new HeaderSkin(this);
    }

    public BooleanBinding hasHistoryProperty() {
        return this.routerService.hasHistoryProperty();
    }

    public ObjectBinding<User> loggedUserProperty() {
        return this.authService.loggedUserProperty();
    }

    public BooleanBinding isLoggedInProperty() {
        return this.authService.isLoggedInProperty();
    }

    public ObjectProperty<AppTheme> appThemeProperty() {
        return this.themeService.appThemeProperty();
    }

    public ObjectBinding<Route> currentRouteProperty() {
        return this.routerService.currentRouteProperty();
    }

    public void onPreviousButtonClick() {
        this.routerService.previous();
    }

    public void onSignOutButtonClick() {
        this.authService.setJwtToken(null);
        this.routerService.navigate(Route.LOGIN);
    }

    public void onSelectedRouteChange(Route route) {
        this.routerService.navigate(route);
    }
}
