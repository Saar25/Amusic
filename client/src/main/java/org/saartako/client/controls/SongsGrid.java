package org.saartako.client.controls;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import org.saartako.song.Song;

public class SongsGrid extends Control {

    private final ListProperty<Song> songs = new SimpleListProperty<>(this, "songs");

    @Override
    protected SongsGridSkin createDefaultSkin() {
        return new SongsGridSkin(this);
    }

    public ListProperty<Song> songsProperty() {
        return this.songs;
    }

    public ObservableList<Song> getSongs() {
        return this.songs.get();
    }
}
