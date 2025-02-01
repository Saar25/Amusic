package org.saartako.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.saartako.client.services.HttpService;
import org.saartako.user.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        final String javaVersion = System.getProperty("java.version");
        final String javafxVersion = System.getProperty("javafx.version");

        final Label l1 = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        final Label l2 = new Label("Loading...");

        final VBox vBox = new VBox(l1, l2);

        Executors.defaultThreadFactory().newThread(() -> {
            final HttpService httpService = new HttpService();
            try {
                final User[] allPeople = httpService.getAllUsers();

                final List<Label> labels = Arrays.stream(allPeople)
                    .map(p -> new Label(p.toString())).toList();

                Platform.runLater(() -> vBox.getChildren().addAll(labels));
                Platform.runLater(() -> {
                    final Optional<Pair<String, String>> result = this.showDialog();

                    if (result.isPresent()) {
                        final String username = result.get().getKey();
                        final String password = result.get().getValue();

                        Executors.defaultThreadFactory().newThread(() -> {
                            try {
                                final User login = httpService.login(username, password);

                                System.out.println(login);
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();

                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Error fetching the people: " + e, ButtonType.OK).showAndWait());
            }
        }).start();

        final StackPane pane = new StackPane(vBox);
        Scene scene = new Scene(pane, 640, 480);
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
