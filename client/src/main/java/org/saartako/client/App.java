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
            } catch (Exception e) {
                Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Error fetching the people: " + e, ButtonType.OK).showAndWait());
            }
        }).start();


        Dialog<Pair<String, Boolean>> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Enter your input and check the box if needed:");

        // Set the button types
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Create input fields
        TextField textField = new TextField();
        textField.setPromptText("Enter text");

        CheckBox checkBox = new CheckBox("Check me");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Text:"), 0, 0);
        grid.add(textField, 1, 0);
        grid.add(checkBox, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert result to a Pair<String, Boolean>
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(textField.getText(), checkBox.isSelected());
            }
            return null;
        });

        // Show dialog and get result
        Optional<Pair<String, Boolean>> result = dialog.showAndWait();
        result.ifPresent(input -> {
            System.out.println("Text: " + input.getKey());
            System.out.println("Checkbox selected: " + input.getValue());
        });

        final StackPane pane = new StackPane(vBox);
        Scene scene = new Scene(pane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}