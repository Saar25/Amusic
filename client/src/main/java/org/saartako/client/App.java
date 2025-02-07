package org.saartako.client;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Theme;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.saartako.client.controls.Amusic;
import org.saartako.client.enums.AppTheme;
import org.saartako.client.services.ThemeService;

import java.net.URL;
import java.util.Objects;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        final Parent root = new Amusic();
        final Scene scene = new Scene(root, Config.APP_WIDTH, Config.APP_HEIGHT);

        final URL resource = getClass().getResource("/styles/styles.css");
        scene.getStylesheets().add(Objects.requireNonNull(resource).toExternalForm());

        final ThemeService themeService = ThemeService.getInstance();
        themeService.appThemeProperty().addListener(
            (o, prev, appTheme) -> setAppTheme(appTheme));
        setAppTheme(themeService.getAppTheme());

        stage.setTitle("Amusic");
        stage.setScene(scene);
        stage.show();
    }

    private void setAppTheme(AppTheme appTheme) {
        final Theme theme = appTheme == AppTheme.LIGHT ? new PrimerLight() : new PrimerDark();
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
    }
}
