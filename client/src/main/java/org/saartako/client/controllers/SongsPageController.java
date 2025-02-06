package org.saartako.client.controllers;

import atlantafx.base.controls.CustomTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.saartako.client.components.SongCard;
import org.saartako.client.services.SongService;
import org.saartako.song.Song;

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
        this.songsGridPane.getChildren().clear();

        for (int i = 0; i < songs.size(); i++) {
            final SongCard songCard = new SongCard();
            songCard.setSong(songs.get(i));

            this.songsGridPane.add(songCard, i % 3, i / 3);
        }
    }
}