package org.saartako.client.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import org.saartako.common.song.Song;

public class SongView extends Control {

    private final ObjectProperty<Song> song = new SimpleObjectProperty<>(this, "song");

    @Override
    protected SongViewSkin createDefaultSkin() {
        return new SongViewSkin(this);
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
