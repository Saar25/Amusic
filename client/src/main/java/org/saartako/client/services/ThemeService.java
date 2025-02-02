package org.saartako.client.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.saartako.client.enums.AppTheme;

public class ThemeService {

    private static ThemeService INSTANCE;

    private final ObjectProperty<AppTheme> appThemeProperty =
        new SimpleObjectProperty<>(this, "appTheme", AppTheme.DARK);

    private ThemeService() {
    }

    public static synchronized ThemeService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThemeService();
        }
        return INSTANCE;
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
}
