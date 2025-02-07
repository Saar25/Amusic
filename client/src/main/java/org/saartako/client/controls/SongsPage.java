package org.saartako.client.controls;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import org.saartako.song.Song;

public class SongsPage extends Control {

    private final ObservableList<? extends Song> songs = new SimpleListProperty<>(this, "songs");

    @Override
    protected SongsPageSkin createDefaultSkin() {
        return new SongsPageSkin(this);
    }

    public ObservableList<? extends Song> songsProperty() {
        return this.songs;
    }
}
