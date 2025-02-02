package org.saartako.client.controllers;

import atlantafx.base.controls.CustomTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import org.saartako.client.services.SongService;

import java.util.Arrays;

public class SongsPageController {

    private final SongService songService = SongService.getInstance();

    private final StringProperty searchProperty = new SimpleStringProperty(this, "searchProperty", "");

    @FXML
    public CustomTextField searchTextField;

    @FXML
    private void initialize() {
        this.searchTextField.textProperty().bindBidirectional(this.searchProperty);

        this.songService.fetchSongs().whenComplete((songs, error) -> {
            if (songs != null) {
                System.out.println("songs: " + Arrays.toString(songs));
            }
            if (error != null) {
                System.err.println("error: " + error.getMessage());
            }
        });
    }
}