package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.saartako.client.Config;

public class UploadSongPageSkin extends SkinBase<UploadSongPage> {

    public UploadSongPageSkin(UploadSongPage control) {
        super(control);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(Config.GAP_MEDIUM);
        gridPane.setVgap(Config.GAP_MEDIUM);

        final Button uploadButton = createUploadButton();
        gridPane.add(uploadButton, 0, 0, 2, 1);

        final Label songNameLabel = new Label("Name:");
        final TextField songNameTextField = new TextField();
        songNameTextField.setPromptText("Enter song name...");
        gridPane.addRow(1, songNameLabel, songNameTextField);

        final Label genreLabel = new Label("Genre:");
        final ComboBox<?> genreComboBox = new ComboBox<>(
            FXCollections.observableArrayList(1, 2, 3, 4));
        genreComboBox.setPlaceholder(new Label("Loading..."));
        genreComboBox.setMaxWidth(Double.MAX_VALUE);
        gridPane.addRow(2, genreLabel, genreComboBox);

        final Label languageLabel = new Label("Language:");
        final ComboBox<?> languageComboBox = new ComboBox<>(
            FXCollections.observableArrayList(1, 2, 3, 4));
        languageComboBox.setPlaceholder(new Label("Loading..."));
        languageComboBox.setMaxWidth(Double.MAX_VALUE);
        gridPane.addRow(3, languageLabel, languageComboBox);

        final VBox vBox = new VBox(Config.GAP_MEDIUM, gridPane);
        vBox.setAlignment(Pos.CENTER_LEFT);

        final Button saveSongButton = createSaveSongButton();

        final HBox hBox = new HBox(Config.GAP_HUGE, vBox, saveSongButton);
        hBox.setAlignment(Pos.CENTER);

        getChildren().setAll(hBox);
    }

    private static Button createSaveSongButton() {
        final Button button = new Button("Save song");
        button.getStyleClass().addAll(Styles.LARGE, Styles.SUCCESS);
        button.setOnAction(event -> System.out.println("click"));
        return button;
    }

    private static Button createUploadButton() {
        final Button button = new Button("Upload", new FontIcon(Material2AL.FILE_UPLOAD));
        button.getStyleClass().addAll(Styles.MEDIUM, Styles.ACCENT);
        button.setOnAction(event -> System.out.println("click"));
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }
}
