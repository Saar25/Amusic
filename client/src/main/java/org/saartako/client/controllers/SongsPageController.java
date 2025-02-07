package org.saartako.client.controllers;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.saartako.client.controls.SongCard;
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
        if (songs == null) {
            final Label label = new Label("Loading...");
            label.getStyleClass().addAll("title-big-1", Styles.TEXT_BOLDER);
            GridPane.setColumnSpan(label, 3);
            this.songsGridPane.getChildren().setAll(label);
        } else {
            final List<Song> filtered = this.songService.filterSongs(songs, filter);

            Platform.runLater(() -> updateSongsInGrid(filtered));
        }
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