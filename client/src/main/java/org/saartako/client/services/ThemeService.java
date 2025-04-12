package org.saartako.client.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.saartako.client.enums.AppTheme;

/**
 * Service for the theme of the application
 */
public class ThemeService {

    private final ObjectProperty<AppTheme> appThemeProperty =
        new SimpleObjectProperty<>(this, "appTheme", AppTheme.DARK);

    private ThemeService() {
    }

    public static ThemeService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ObjectProperty<AppTheme> appThemeProperty() {
        return this.appThemeProperty;
    }

    public AppTheme getAppTheme() {
        return this.appThemeProperty.getValue();
    }

    public void setAppTheme(AppTheme appTheme) {
        this.appThemeProperty.setValue(appTheme);
    }

    private static final class InstanceHolder {
        private static final ThemeService INSTANCE = new ThemeService();
    }
}
