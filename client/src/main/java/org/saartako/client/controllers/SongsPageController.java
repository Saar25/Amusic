package org.saartako.client.controllers;

import atlantafx.base.controls.CustomTextField;
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

    @FXML
    private CustomTextField searchTextField;

    @FXML
    private GridPane songsGridPane;

    @FXML
    private void initialize() {
        this.searchTextField.textProperty().addListener((observable, oldValue, filter) ->
            updateSongsInGrid(this.songService.songsProperty().getValue(), filter));

        this.songService.songsProperty().addListener((observable, oldValue, songs) ->
            updateSongsInGrid(songs, this.searchTextField.textProperty().getValue()));

        updateSongsInGrid(
            this.songService.songsProperty().getValue(),
            this.searchTextField.textProperty().getValue());
    }

    private void updateSongsInGrid(List<Song> songs, String filter) {
        final List<Song> filtered = songs == null
            ? List.of()
            : this.songService.filterSongs(songs, filter);

        updateSongsInGrid(filtered);
    }

    private void updateSongsInGrid(List<Song> songs) {
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