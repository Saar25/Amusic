package org.saartako.client.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import org.saartako.song.Song;

public class SongController {

    private ObjectProperty<Song> songProperty = new SimpleObjectProperty<>(null, "song", null);

    @FXML
    private void initialize() {
    }

    public void setSong(Song song) {
        this.songProperty.setValue(song);
    }
}