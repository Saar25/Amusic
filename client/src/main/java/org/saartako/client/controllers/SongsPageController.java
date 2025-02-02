package org.saartako.client.controllers;

import atlantafx.base.controls.CustomTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.saartako.client.services.SongService;
import org.saartako.song.Song;

import java.io.IOException;
import java.util.List;

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

        this.songService.songsProperty().addListener((observable, oldValue, songs) ->
            Platform.runLater(() -> addSongsToGrid(songs)));
    }

    private void addSongsToGrid(List<Song> songs) {
        try {
            this.songsGridPane.getChildren().clear();

            for (int i = 0; i < songs.size(); i++) {
                final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/components/song.fxml"));
                final Node songNode = fxmlLoader.load();

                final SongController controller = fxmlLoader.getController();
                controller.setSong(songs.get(i));

                this.songsGridPane.add(songNode, i % 3, i / 3);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}