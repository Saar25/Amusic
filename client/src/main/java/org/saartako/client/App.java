package org.saartako.client;

import atlantafx.base.theme.CupertinoLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.saartako.client.services.HttpService;
import org.saartako.user.User;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.xml"));
        final Parent root = loader.load();

        final StackPane pane = new StackPane(root);
        Scene scene = new Scene(pane, 640, 480);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());

        stage.setScene(scene);
        stage.show();
    }

    private User login() throws ExecutionException, InterruptedException {
        final HttpService httpService = new HttpService();

        final Optional<Pair<String, String>> result = this.showDialog();

        if (result.isEmpty()) {
            return null;
        }

        final String username = result.get().getKey();
        final String password = result.get().getValue();

        final CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return httpService.login(username, password);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return userFuture.get();

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
