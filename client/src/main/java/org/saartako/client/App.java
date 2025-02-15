package org.saartako.client;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Theme;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
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
        final Parent root = new StackPane(new Amusic());
        final Scene scene = new Scene(root, Config.APP_WIDTH, Config.APP_HEIGHT);

        final URL resource = getClass().getResource("/styles/styles.css");
        scene.getStylesheets().add(Objects.requireNonNull(resource).toExternalForm());

        final ThemeService themeService = ThemeService.getInstance();
        themeService.appThemeProperty().addListener((o, prev, appTheme) -> {
            animateThemeChange(scene);
            setAppTheme(appTheme);
        });
        setAppTheme(themeService.getAppTheme());

        stage.setTitle("Amusic");
        stage.setScene(scene);
        stage.show();
    }

    private void setAppTheme(AppTheme appTheme) {
        final Theme theme = appTheme == AppTheme.LIGHT ? new PrimerLight() : new PrimerDark();
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
    }

    private void animateThemeChange(Scene scene) {
        final Image snapshot = scene.snapshot(null);
        final Pane root = (Pane) scene.getRoot();

        final ImageView imageView = new ImageView(snapshot);
        root.getChildren().add(imageView);

        final Timeline transition = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(imageView.opacityProperty(), 1, Interpolator.EASE_OUT)),
            new KeyFrame(Duration.millis(500), new KeyValue(imageView.opacityProperty(), 0, Interpolator.EASE_OUT))
        );
        transition.setOnFinished(e -> root.getChildren().remove(imageView));
        transition.play();
    }
}
