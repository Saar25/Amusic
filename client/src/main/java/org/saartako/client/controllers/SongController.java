package org.saartako.client.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.saartako.client.utils.SongUtils;
import org.saartako.song.Song;

public class SongController {

    private final ObjectProperty<Song> songProperty = new SimpleObjectProperty<>(null, "song", null);

    @FXML
    private Pane cardBodyPane;

    @FXML
    private Label songNameLabel;

    @FXML
    private VBox footerVBox;

    @FXML
    private void initialize() {
        this.songProperty.addListener((o, prev, song) -> {
            this.cardBodyPane.setStyle("-fx-background-color: " + SongUtils.getSongColor(song)
                                       + "; -fx-background-radius: 4 4 0 0;");

            if (song != null) {
                this.songNameLabel.setText(song.getName());
            }

            this.footerVBox.getChildren().clear();

            if (song != null && song.getUploader() != null) {
                this.footerVBox.getChildren().add(new Label(
                    "By: " + song.getUploader().getDisplayName()
                ));
            }
            if (song != null) {
                this.footerVBox.getChildren().add(new Label(
                    "Length: " + "TODO"
                ));
            }
            if (song != null && song.getGenre() != null) {
                this.footerVBox.getChildren().add(new Label(
                    "Genre: " + song.getGenre().getName()
                ));
            }
            if (song != null && song.getLanguage() != null) {
                this.footerVBox.getChildren().add(new Label(
                    "Language: " + song.getLanguage().getName()
                ));
            }
        });
    }

    public void setSong(Song song) {
        this.songProperty.setValue(song);
    }
}