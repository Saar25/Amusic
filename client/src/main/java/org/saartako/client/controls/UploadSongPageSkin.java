package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.beans.property.ListProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.saartako.client.Config;
import org.saartako.common.genre.Genre;
import org.saartako.common.language.Language;

import java.util.Objects;
import java.util.Optional;

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
        final ComboBox<Genre> genreComboBox = new ComboBox<>(getSkinnable().genresProperty());
        genreComboBox.setPlaceholder(new Label("Loading..."));
        genreComboBox.setMaxWidth(Double.MAX_VALUE);
        genreComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Genre object) {
                return object == null ? "Select genre..." : object.getName();
            }

            @Override
            public Genre fromString(String string) {
                final ListProperty<Genre> genres = getSkinnable().genresProperty();
                final Optional<Genre> genreOpt = genres.stream()
                    .filter(l -> Objects.equals(l.getName(), string)).findAny();
                return genreOpt.orElse(null);
            }
        });
        gridPane.addRow(2, genreLabel, genreComboBox);

        final Label languageLabel = new Label("Language:");
        final ComboBox<Language> languageComboBox = new ComboBox<>(getSkinnable().languagesProperty());
        languageComboBox.setPlaceholder(new Label("Loading..."));
        languageComboBox.setMaxWidth(Double.MAX_VALUE);
        languageComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Language object) {
                return object == null ? "Select language..." : object.getName();
            }

            @Override
            public Language fromString(String string) {
                final ListProperty<Language> languages = getSkinnable().languagesProperty();
                final Optional<Language> languageOpt = languages.stream()
                    .filter(l -> Objects.equals(l.getName(), string)).findAny();
                return languageOpt.orElse(null);
            }
        });
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
