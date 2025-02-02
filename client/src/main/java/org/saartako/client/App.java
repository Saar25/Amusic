package org.saartako.client;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.saartako.client.components.Router;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        final RouterProvider routerProvider = new RouterProvider();
        final Router router = routerProvider.createRouter();
        final Scene scene = new Scene(router, Config.APP_WIDTH, Config.APP_HEIGHT);

        final URL resource = getClass().getResource("/styles/styles.css");
        scene.getStylesheets().add(Objects.requireNonNull(resource).toExternalForm());

        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());

        stage.setScene(scene);
        stage.show();
    }
}
