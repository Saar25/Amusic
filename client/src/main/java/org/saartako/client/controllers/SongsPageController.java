package org.saartako.client.controllers;

import atlantafx.base.controls.CustomTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
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

    private ObjectExpression<List<Song>> filteredSongs;

    @FXML
    private CustomTextField searchTextField;

    @FXML
    private GridPane songsGridPane;

    @FXML
    private void initialize() {
        this.filteredSongs = Bindings.createObjectBinding(() -> {
            final List<Song> songs = this.songService.songsProperty().getValue();
            if (songs == null) {
                return List.of();
            }

            final String search = this.searchTextField.textProperty().getValue();
            return songs.stream().filter(song ->
                song.getName().contains(search) ||
                (song.getUploader() != null && song.getUploader().getDisplayName().contains(search)) ||
                (song.getGenre() != null && song.getGenre().getName().contains(search))
            ).toList();
        }, this.searchTextField.textProperty(), this.songService.songsProperty());

        this.filteredSongs.addListener((observable, oldValue, songs) -> {
            addSongsToGrid(songs);
        });
        if (this.filteredSongs.getValue() != null) {
            addSongsToGrid(this.filteredSongs.getValue());
        }
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