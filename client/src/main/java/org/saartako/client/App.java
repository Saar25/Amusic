package org.saartako.client;

import atlantafx.base.theme.CupertinoLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.saartako.client.components.Router;
import org.saartako.client.constants.Route;
import org.saartako.client.services.RouterService;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        final Router router = new Router(Map.ofEntries(
            Map.entry(Route.LOGIN, new FXMLLoader(getClass().getResource("/views/login.fxml")).load()),
            Map.entry(Route.TMP, new FXMLLoader(getClass().getResource("/views/tmp.fxml")).load()),
            Map.entry(Route.TEST, new FXMLLoader(getClass().getResource("/views/test.fxml")).load())
        ));

        final RouterService routerService = RouterService.getInstance();
        routerService.setCurrentRoute(Route.LOGIN);

        final Scene scene = new Scene(router, 1024, 720);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());

        stage.setScene(scene);
        stage.show();
    }

    private Optional<Pair<String, String>> showDialog() {
        final Dialog<Pair<String, String>> dialog = new Dialog<>();

        dialog.setTitle("Login form");
        dialog.setHeaderText("Enter username and password");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        final TextField passwordField = new TextField();
        passwordField.setPromptText("Enter password");

        final GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Text:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            final String username = usernameField.getText();
            final String password = passwordField.getText();
            return dialogButton == ButtonType.OK ? new Pair<>(username, password) : null;
        });

        return dialog.showAndWait();
    }
}
