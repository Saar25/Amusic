package org.saartako.client.controllers;

import atlantafx.base.controls.CustomTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import org.saartako.client.services.SongService;
import org.saartako.song.Song;

import java.io.IOException;

public class SongsPageController {

    private final SongService songService = SongService.getInstance();

    private final StringProperty searchProperty = new SimpleStringProperty(this, "searchProperty", "");

    @FXML
    private CustomTextField searchTextField;

    @FXML
    private GridPane songsGridPane;

    @FXML
    private void initialize() {
        this.searchTextField.textProperty().bindBidirectional(this.searchProperty);

        this.songService.fetchSongs().whenComplete((songs, error) -> {
            if (songs != null) {
                Platform.runLater(() -> addSongsToGrid(songs));
            }
            if (error != null) {
                Platform.runLater(() -> {
                    final Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Failed to fetch songs\n" + error.getMessage());
                    alert.show();
                });
            }
        });
    }

    private void addSongsToGrid(Song[] songs) {
        try {
            this.songsGridPane.getChildren().clear();

            for (int i = 0; i < songs.length; i++) {
                final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/components/song.fxml"));
                final Node songNode = fxmlLoader.load();

                final SongController controller = fxmlLoader.getController();
                controller.setSong(songs[i]);

                this.songsGridPane.add(songNode, i % 3, i / 3);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}