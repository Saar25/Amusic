package org.saartako.client.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import org.saartako.song.Song;

public class SongCard extends Control {

    private final ObjectProperty<Song> song = new SimpleObjectProperty<>(this, "song");

    @Override
    protected SongCardSkin createDefaultSkin() {
        return new SongCardSkin(this);
    }

    public ObjectProperty<Song> songProperty() {
        return this.song;
    }

    public Song getSong() {
        return this.song.get();
    }

    public void setSong(Song song) {
        this.song.set(song);
    }
}
